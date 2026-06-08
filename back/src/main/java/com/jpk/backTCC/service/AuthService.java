package com.jpk.backTCC.service;

import com.jpk.backTCC.dto.LoginRequestDTO;
import com.jpk.backTCC.dto.LoginResponseDTO;
import com.jpk.backTCC.entity.Usuario;
import com.jpk.backTCC.repository.UsuarioRepository;
import com.jpk.backTCC.security.CustomUserDetailsService;
import com.jpk.backTCC.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       CustomUserDetailsService userDetailsService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLoginAndAtivoTrue(dto.getLogin())
                .orElseThrow(() -> credenciaisInvalidas());

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash())) {
            throw credenciaisInvalidas();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getLogin());
        String token = jwtService.gerarToken(userDetails);

        return new LoginResponseDTO(token, usuario.getId(), usuario.getLogin());
    }

    private ResponseStatusException credenciaisInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login ou senha invalidos.");
    }
}
