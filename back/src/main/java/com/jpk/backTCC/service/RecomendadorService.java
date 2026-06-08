package com.jpk.backTCC.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpk.backTCC.dto.CriarRecomendacaoDTO;
import com.jpk.backTCC.dto.CriarOrdemManipulacaoDTO;
import com.jpk.backTCC.dto.ExcipienteDTO;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RecomendadorService {

    private final ObjectMapper objectMapper;
    private final ChatModel chatModel;

    public RecomendadorService(
            ObjectMapper objectMapper,
            @Value("${langchain4j.google-ai-gemini.chat-model.api-key:}") String apiKey,
            @Value("${langchain4j.google-ai-gemini.chat-model.model-name:}") String modelName) {
        this.objectMapper = objectMapper;

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "API KEY nao definida. Configure 'langchain4j.google-ai-gemini.chat-model.api-key' " +
                    "(ex.: via variavel GEMINI_API_KEY)."
            );
        }

        if (modelName == null || modelName.isBlank()) {
            throw new IllegalStateException(
                "Modelo de IA nao definido. Configure 'langchain4j.google-ai-gemini.chat-model.model-name' " +
                "(ex.: via variavel GEMINI_MODEL_NAME)."
            );
        }

        this.chatModel = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .build();
    }

    public CriarRecomendacaoDTO gerarRecomendacao(CriarOrdemManipulacaoDTO ordemManipulacao) {
        String prompt = montarPrompt(ordemManipulacao);
        String resposta = chamarModelo(prompt);

        try {
            String json = limparRespostaModelo(resposta);
            return objectMapper.readValue(json, CriarRecomendacaoDTO.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Nao foi possivel interpretar a resposta do modelo de IA.", e);
        }
    }

    private String chamarModelo(String prompt) {
        try {
            String resposta = chatModel.chat(prompt);
            return resposta == null ? "{}" : resposta;
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao executar o modelo de IA via LangChain4j.", e);
        }
    }

    private String montarPrompt(CriarOrdemManipulacaoDTO ordemManipulacao) {
        return """
                Voce e um assistente farmacotecnico. OrdemManipulacao os dados e retorne recomendacoes.
                Responda exclusivamente em JSON valido, sem markdown, sem texto extra.

                Formato esperado:
                {
                  "recomendacoesPrincipiosAtivos": [{"nivel": "grave|medio|leve|ok", "mensagem": "..."}],
                  "recomendacoesExcipientes": [{"nivel": "grave|medio|leve|ok", "mensagem": "..."}],
                  "recomendacoesEmbalagens": [{"nivel": "grave|medio|leve|ok", "mensagem": "..."}],
                  "recomendacaoGeral": {"nivel": "grave|medio|leve|ok", "mensagem": "..."}
                }

                Regras:
                - Mantenha a mesma quantidade de itens de recomendacoesPrincipiosAtivos do input de principios ativos.
                - Mantenha a mesma quantidade de itens de recomendacoesExcipientes do input de excipientes.
                - Mantenha a mesma quantidade de itens de recomendacoesEmbalagens do input de embalagens.
                - Mensagens curtas, objetivas e tecnicas.
                - Fique estritamente no escopo tecnico da formulacao recebida.
                - Principios ativos podem vir em: mg, ml, mcg, ui ou %%.
                - Excipientes podem vir em: mg, ml, mcg, ui, %% ou qsp.
                - Quando um excipiente vier como qsp, trate como "quantidade suficiente para" (sem dose numerica).
                - Nao use linguagem alarmista (ex.: "urgente", "urgentemente").
                - Nao solicite investigacoes clinicas amplas fora dos dados enviados.
                - Se faltar informacao, diga "dados insuficientes para concluir" e limite-se ao impacto tecnico.

                Dados da ordemManipulacao:
                Idade: %d
                Sexo: %s
                Peso: %.2f
                Frequencia de uso: a cada %s
                Embalagens: %s
                Principios ativos: %s
                Excipientes: %s
                """.formatted(
                ordemManipulacao.getIdade(),
                ordemManipulacao.getSexo(),
                ordemManipulacao.getPeso(),
                formatarFrequenciaDose(ordemManipulacao),
                formatarEmbalagens(ordemManipulacao.getEmbalagens()),
                formatarItens(ordemManipulacao.getPrincipiosAtivos()),
                formatarItens(ordemManipulacao.getExcipientes())
        );
    }

    private String formatarFrequenciaDose(CriarOrdemManipulacaoDTO ordemManipulacao) {
        if (ordemManipulacao.getFrequencia_dose() == null || ordemManipulacao.getTipo_frequencia_dose() == null || ordemManipulacao.getTipo_frequencia_dose().isBlank()) {
            return "dados insuficientes para concluir";
        }
        return "%d %s".formatted(ordemManipulacao.getFrequencia_dose(), ordemManipulacao.getTipo_frequencia_dose());
    }

    private String formatarEmbalagens(List<String> embalagens) {
        if (embalagens == null || embalagens.isEmpty()) {
            return "[]";
        }
        return embalagens.stream().map(item -> item == null || item.isBlank() ? "sem nome" : item.trim())
                .collect(Collectors.joining(", "));
    }

    private String formatarItens(List<ExcipienteDTO> itens) {
        if (itens == null || itens.isEmpty()) {
            return "[]";
        }
        return itens.stream()
                .map(item -> {
                    String unidade = item.getTipoMedida() == null ? "" : item.getTipoMedida().trim();
                    if ("qsp".equalsIgnoreCase(unidade)) {
                        return "%s (QSP)".formatted(item.getNome());
                    }
                    return "%s (%s %s)".formatted(item.getNome(), item.getQuantidade(), item.getTipoMedida());
                })
                .collect(Collectors.joining(", "));
    }

    private String limparRespostaModelo(String resposta) {
        if (resposta == null) {
            return "{}";
        }
        String semBlocos = resposta
                .replace("```json", "")
                .replace("```", "")
                .trim();

        int inicio = semBlocos.indexOf('{');
        int fim = semBlocos.lastIndexOf('}');

        if (inicio >= 0 && fim > inicio) {
            return semBlocos.substring(inicio, fim + 1);
        }

        return semBlocos;
    }
}
