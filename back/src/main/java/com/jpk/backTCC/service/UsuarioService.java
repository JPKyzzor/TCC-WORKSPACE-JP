package com.jpk.backTCC.service;

import com.jpk.backTCC.dto.UsuarioOpcaoDTO;
import com.jpk.backTCC.entity.Usuario;
import com.jpk.backTCC.repository.UsuarioRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private static final long ADMIN_USER_ID = 1L;

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioAutenticadoService usuarioAutenticadoService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(readOnly = true)
    public List<UsuarioOpcaoDTO> listarUsuariosParaFiltro() {
        validarAcessoAdmin();

        return usuarioRepository.findByAtivoTrueOrderByIdAsc()
                .stream()
                .map(usuario -> new UsuarioOpcaoDTO(usuario.getId(), usuario.getLogin()))
                .toList();
    }

    private void validarAcessoAdmin() {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        if (usuarioLogado.getId() == null || usuarioLogado.getId() != ADMIN_USER_ID) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso restrito ao administrador.");
        }
    }
}