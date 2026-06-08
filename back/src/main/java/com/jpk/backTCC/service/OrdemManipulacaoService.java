package com.jpk.backTCC.service;

import com.jpk.backTCC.dto.*;
import com.jpk.backTCC.entity.OrdemManipulacao;
import com.jpk.backTCC.entity.OrdemManipulacaoEmbalagem;
import com.jpk.backTCC.entity.OrdemManipulacaoExcipiente;
import com.jpk.backTCC.entity.OrdemManipulacaoPrincipioAtivo;
import com.jpk.backTCC.entity.Recomendacao;
import com.jpk.backTCC.entity.RecomendacaoEmbalagem;
import com.jpk.backTCC.entity.RecomendacaoExcipiente;
import com.jpk.backTCC.entity.RecomendacaoPrincipioAtivo;
import com.jpk.backTCC.entity.Usuario;
import com.jpk.backTCC.entity.enums.NivelEnum;
import com.jpk.backTCC.entity.enums.TipoFrequenciaDose;
import com.jpk.backTCC.entity.enums.UnidadeMedida;
import com.jpk.backTCC.repository.OrdemManipulacaoEmbalagemRepository;
import com.jpk.backTCC.repository.OrdemManipulacaoExcipienteRepository;
import com.jpk.backTCC.repository.OrdemManipulacaoPrincipioAtivoRepository;
import com.jpk.backTCC.repository.OrdemManipulacaoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdemManipulacaoService {

    private final OrdemManipulacaoRepository ordemManipulacaoRepository;
    private final OrdemManipulacaoEmbalagemRepository embalagemRepository;
    private final OrdemManipulacaoExcipienteRepository excipienteRepository;
    private final OrdemManipulacaoPrincipioAtivoRepository principioAtivoRepository;
    private final RecomendadorService recomendadorService;
    private final RecomendacaoService recomendacaoService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public OrdemManipulacaoService(OrdemManipulacaoRepository ordemManipulacaoRepository,
                                   OrdemManipulacaoEmbalagemRepository embalagemRepository,
                                   OrdemManipulacaoExcipienteRepository excipienteRepository,
                                   OrdemManipulacaoPrincipioAtivoRepository principioAtivoRepository,
                                   RecomendadorService recomendadorService,
                                   RecomendacaoService recomendacaoService,
                                   UsuarioAutenticadoService usuarioAutenticadoService) {
        this.ordemManipulacaoRepository = ordemManipulacaoRepository;
        this.embalagemRepository = embalagemRepository;
        this.excipienteRepository = excipienteRepository;
        this.principioAtivoRepository = principioAtivoRepository;
        this.recomendadorService = recomendadorService;
        this.recomendacaoService = recomendacaoService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional
    public OrdemManipulacaoResponseDTO criarOrdemManipulacao(CriarOrdemManipulacaoDTO dto) {
        OrdemManipulacao ordemManipulacaoSalva = salvarOrdemManipulacao(dto);

        CriarRecomendacaoDTO respostaIA = recomendadorService.gerarRecomendacao(dto);

        Recomendacao recomendacao = toRecomendacaoEntity(respostaIA, ordemManipulacaoSalva);
        Recomendacao recomendacaoSalva = recomendacaoService.criarRecomendacao(recomendacao);

        return toOrdemManipulacaoResponseDTO(ordemManipulacaoSalva, recomendacaoSalva);
    }

    private OrdemManipulacao salvarOrdemManipulacao(CriarOrdemManipulacaoDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        OrdemManipulacao ordemManipulacao = new OrdemManipulacao();
        ordemManipulacao.setIdade(dto.getIdade());
        ordemManipulacao.setPeso(dto.getPeso());
        ordemManipulacao.setSexo(dto.getSexo());
        ordemManipulacao.setTipoFrequenciaDose(parseTipoFrequenciaDose(dto.getTipo_frequencia_dose()));
        ordemManipulacao.setFrequenciaDose(dto.getFrequencia_dose());
        ordemManipulacao.setUsuario(usuarioLogado);
        OrdemManipulacao ordemManipulacaoSalva = ordemManipulacaoRepository.save(ordemManipulacao);

        if (dto.getEmbalagens() != null) {
            List<OrdemManipulacaoEmbalagem> embalagens = java.util.stream.IntStream.range(0, dto.getEmbalagens().size())
                    .mapToObj(i -> toOrdemManipulacaoEmbalagem(dto.getEmbalagens().get(i), ordemManipulacaoSalva, i + 1))
                    .toList();
            embalagemRepository.saveAll(embalagens);
        }

        if (dto.getExcipientes() != null) {
            List<ExcipienteDTO> lista = dto.getExcipientes();
            List<OrdemManipulacaoExcipiente> excipientes = java.util.stream.IntStream.range(0, lista.size())
                    .mapToObj(i -> toOrdemManipulacaoExcipiente(lista.get(i), ordemManipulacaoSalva, i + 1))
                    .toList();
            excipienteRepository.saveAll(excipientes);
        }

        if (dto.getPrincipiosAtivos() != null) {
            List<ExcipienteDTO> lista = dto.getPrincipiosAtivos();
            List<OrdemManipulacaoPrincipioAtivo> principiosAtivos = java.util.stream.IntStream.range(0, lista.size())
                    .mapToObj(i -> toOrdemManipulacaoPrincipioAtivo(lista.get(i), ordemManipulacaoSalva, i + 1))
                    .toList();
            principioAtivoRepository.saveAll(principiosAtivos);
        }

        return ordemManipulacaoSalva;
    }

    private Recomendacao toRecomendacaoEntity(CriarRecomendacaoDTO dto, OrdemManipulacao ordemManipulacao) {
        Recomendacao recomendacao = new Recomendacao();
        recomendacao.setOrdemManipulacao(ordemManipulacao);

        if (dto.getRecomendacaoGeral() != null) {
            recomendacao.setMensagemGeral(dto.getRecomendacaoGeral().getMensagem());
            recomendacao.setNivelGeral(toNivelEnum(dto.getRecomendacaoGeral().getNivel()));
        }

        List<RecomendacaoExcipiente> excipientes = new ArrayList<>();
        if (dto.getRecomendacoesExcipientes() != null) {
            for (RecomendacaoDTO item : dto.getRecomendacoesExcipientes()) {
                RecomendacaoExcipiente excipiente = new RecomendacaoExcipiente();
                excipiente.setNivel(toNivelEnum(item.getNivel()));
                excipiente.setMensagem(item.getMensagem());
                excipientes.add(excipiente);
            }
        }
        recomendacao.setRecomendacoesExcipientes(excipientes);

        List<RecomendacaoPrincipioAtivo> principiosAtivos = new ArrayList<>();
        if (dto.getRecomendacoesPrincipiosAtivos() != null) {
            for (RecomendacaoDTO item : dto.getRecomendacoesPrincipiosAtivos()) {
                RecomendacaoPrincipioAtivo principioAtivo = new RecomendacaoPrincipioAtivo();
                principioAtivo.setNivel(toNivelEnum(item.getNivel()));
                principioAtivo.setMensagem(item.getMensagem());
                principiosAtivos.add(principioAtivo);
            }
        }
        recomendacao.setRecomendacoesPrincipiosAtivos(principiosAtivos);

        List<RecomendacaoEmbalagem> embalagens = new ArrayList<>();
        if (dto.getRecomendacoesEmbalagens() != null) {
            for (RecomendacaoDTO item : dto.getRecomendacoesEmbalagens()) {
                RecomendacaoEmbalagem embalagem = new RecomendacaoEmbalagem();
                embalagem.setNivel(toNivelEnum(item.getNivel()));
                embalagem.setMensagem(item.getMensagem());
                embalagens.add(embalagem);
            }
        }
        recomendacao.setRecomendacoesEmbalagens(embalagens);

        return recomendacao;
    }

    private OrdemManipulacaoResponseDTO toOrdemManipulacaoResponseDTO(OrdemManipulacao ordemManipulacao, Recomendacao recomendacao) {
        OrdemManipulacaoResponseDTO dto = new OrdemManipulacaoResponseDTO();
        dto.setOrdemManipulacaoId(ordemManipulacao.getId());
        dto.setRecomendacaoGeral(new RecomendacaoComIdDTO(
                recomendacao.getId(),
                toNivelRecomendacao(recomendacao.getNivelGeral()),
                recomendacao.getMensagemGeral()
        ));

        dto.setRecomendacoesPrincipiosAtivos(recomendacao.getRecomendacoesPrincipiosAtivos().stream()
                .map(item -> new RecomendacaoComIdDTO(item.getId(), toNivelRecomendacao(item.getNivel()), item.getMensagem()))
                .toList());

        dto.setRecomendacoesExcipientes(recomendacao.getRecomendacoesExcipientes().stream()
                .map(item -> new RecomendacaoComIdDTO(item.getId(), toNivelRecomendacao(item.getNivel()), item.getMensagem()))
                .toList());

        dto.setRecomendacoesEmbalagens(recomendacao.getRecomendacoesEmbalagens().stream()
                .map(item -> new RecomendacaoComIdDTO(item.getId(), toNivelRecomendacao(item.getNivel()), item.getMensagem()))
                .toList());

        return dto;
    }

    private NivelEnum toNivelEnum(NivelRecomendacao nivel) {
        if (nivel == null) {
            return NivelEnum.OK;
        }

        return switch (nivel) {
            case grave -> NivelEnum.GRAVE;
            case medio -> NivelEnum.MEDIO;
            case leve -> NivelEnum.LEVE;
            case ok -> NivelEnum.OK;
        };
    }

    private NivelRecomendacao toNivelRecomendacao(NivelEnum nivel) {
        if (nivel == null) {
            return NivelRecomendacao.ok;
        }

        return switch (nivel) {
            case GRAVE -> NivelRecomendacao.grave;
            case MEDIO -> NivelRecomendacao.medio;
            case LEVE -> NivelRecomendacao.leve;
            case OK -> NivelRecomendacao.ok;
        };
    }

    private OrdemManipulacaoEmbalagem toOrdemManipulacaoEmbalagem(String embalagem, OrdemManipulacao ordemManipulacao, int ordem) {
        OrdemManipulacaoEmbalagem entity = new OrdemManipulacaoEmbalagem();
        entity.setOrdem(ordem);
        entity.setNome(embalagem);
        entity.setOrdemManipulacao(ordemManipulacao);
        return entity;
    }

    private OrdemManipulacaoExcipiente toOrdemManipulacaoExcipiente(ExcipienteDTO dto, OrdemManipulacao ordemManipulacao, int ordem) {
        OrdemManipulacaoExcipiente entity = new OrdemManipulacaoExcipiente();
        UnidadeMedida unidade = parseUnidadeExcipiente(dto.getTipoMedida());
        entity.setOrdem(ordem);
        entity.setNome(dto.getNome());
        entity.setQuantidade(parseQuantidadeExcipiente(dto.getQuantidade(), unidade));
        entity.setUnidade(unidade);
        entity.setOrdemManipulacao(ordemManipulacao);
        return entity;
    }

    private OrdemManipulacaoPrincipioAtivo toOrdemManipulacaoPrincipioAtivo(ExcipienteDTO dto, OrdemManipulacao ordemManipulacao, int ordem) {
        OrdemManipulacaoPrincipioAtivo entity = new OrdemManipulacaoPrincipioAtivo();
        UnidadeMedida unidade = parseUnidadePrincipioAtivo(dto.getTipoMedida());
        entity.setOrdem(ordem);
        entity.setNome(dto.getNome());
        entity.setQuantidade(parseQuantidadeObrigatoria(dto.getQuantidade(), "principio ativo"));
        entity.setUnidade(unidade);
        entity.setOrdemManipulacao(ordemManipulacao);
        return entity;
    }

    private UnidadeMedida parseUnidadePrincipioAtivo(String tipoMedida) {
        UnidadeMedida unidade = parseUnidade(tipoMedida);
        return switch (unidade) {
            case MG, ML, MCG, UI, PCT -> unidade;
            case QSP -> throw new IllegalArgumentException("QSP e permitido apenas para excipientes.");
        };
    }

    private UnidadeMedida parseUnidadeExcipiente(String tipoMedida) {
        return parseUnidade(tipoMedida);
    }

    private UnidadeMedida parseUnidade(String tipoMedida) {
        if (tipoMedida == null || tipoMedida.isBlank()) {
            return UnidadeMedida.MG;
        }

        String valorNormalizado = tipoMedida.trim().toLowerCase(Locale.ROOT);
        return switch (valorNormalizado) {
            case "mg" -> UnidadeMedida.MG;
            case "ml" -> UnidadeMedida.ML;
            case "mcg" -> UnidadeMedida.MCG;
            case "ui" -> UnidadeMedida.UI;
            case "%", "pct" -> UnidadeMedida.PCT;
            case "qsp" -> UnidadeMedida.QSP;
            default -> throw new IllegalArgumentException("tipoMedida invalido. Valores aceitos: mg, ml, mcg, ui, %, qsp.");
        };
    }

    private Double parseQuantidadeObrigatoria(Double quantidade, String contexto) {
        if (quantidade == null) {
            throw new IllegalArgumentException("Quantidade obrigatoria para " + contexto + ".");
        }
        return quantidade;
    }

    private Double parseQuantidadeExcipiente(Double quantidade, UnidadeMedida unidade) {
        if (unidade == UnidadeMedida.QSP) {
            return 0d;
        }
        return parseQuantidadeObrigatoria(quantidade, "excipiente");
    }

    private TipoFrequenciaDose parseTipoFrequenciaDose(String tipoFrequenciaDose) {
        if (tipoFrequenciaDose == null || tipoFrequenciaDose.isBlank()) {
            return null;
        }

        String valorNormalizado = tipoFrequenciaDose.trim().toUpperCase(Locale.ROOT);
        try {
            return TipoFrequenciaDose.valueOf(valorNormalizado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("tipo_frequencia_dose invalido. Use: dia, semana ou mes.");
        }
    }
}
