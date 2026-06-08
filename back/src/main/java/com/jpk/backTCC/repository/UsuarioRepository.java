package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLoginAndAtivoTrue(String login);
    List<Usuario> findByAtivoTrueOrderByIdAsc();
}
