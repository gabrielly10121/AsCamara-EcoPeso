package br.com.ascamaras.Repositories;

import br.com.ascamaras.Model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    Optional<UsuarioModel> findByUsername(String username);
    Optional<UsuarioModel> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM UsuarioModel u WHERE u.email = :email AND u.senha = :senha")
    Optional<UsuarioModel> findByEmailAndSenha(@Param("email") String email, @Param("senha") String senha);
}