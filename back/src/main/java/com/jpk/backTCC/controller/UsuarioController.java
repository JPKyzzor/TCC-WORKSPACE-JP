package com.jpk.backTCC.controller;

import com.jpk.backTCC.dto.UsuarioOpcaoDTO;
import com.jpk.backTCC.service.UsuarioService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioOpcaoDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuariosParaFiltro());
    }
}