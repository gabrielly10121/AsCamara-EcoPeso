package br.com.ascamaras.Services;

import br.com.ascamaras.Dtos.UsuarioDto;
import br.com.ascamaras.Model.UsuarioModel;
import br.com.ascamaras.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioModel salvarUsuario(UsuarioDto usuarioDto) {
        validarDadosUsuario(usuarioDto);

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(usuarioDto.getNome());
        usuario.setSobrenome(usuarioDto.getSobrenome() != null ? usuarioDto.getSobrenome() : "");
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setUsername(gerarUsername(usuarioDto.getEmail()));
        usuario.setRole("ROLE_USER");
        usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));

        return usuarioRepository.save(usuario);

}

    private void validarDadosUsuario(UsuarioDto usuarioDto) {
        if (usuarioRepository.existsByUsername(gerarUsername(usuarioDto.getEmail()))){
            throw new IllegalArgumentException("Nome de usuário já está em uso");
        }

        if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        if (!usuarioDto.getSenha().equals(usuarioDto.getConfirmarSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem");
        }

        if (usuarioDto.getSenha().length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres");
        }



}

    @Transactional
    public UsuarioModel criarUsuario(UsuarioDto usuarioDto) {

        if (!validarEmail(usuarioDto.getEmail())) {
            throw new IllegalArgumentException("O email precisa terminar com @gmail.com");
        }


        if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }


        if (!usuarioDto.getSenha().equals(usuarioDto.getConfirmarSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem");
        }

        if (usuarioDto.getSenha().length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres");
        }


        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(usuarioDto.getNome());
        usuario.setSobrenome(
                usuarioDto.getSobrenome() != null ? usuarioDto.getSobrenome() : "");
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setUsername(gerarUsername(usuarioDto.getEmail()));
        usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
        usuario.setRole(usuarioDto.getRole() != null ? usuarioDto.getRole() : "ROLE_USER");


        return usuarioRepository.save(usuario);

}

    @Transactional
    public UsuarioModel atualizarUsuario(Long id, UsuarioDto usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioDTO.getNome());
                    usuario.setSobrenome(usuarioDTO.getSobrenome() != null ? usuarioDTO.getSobrenome() : usuario.getSobrenome());
                    usuario.setEmail(usuarioDTO.getEmail());

                    if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
                        System.out.println("Senha vazia, menssagem do UsuarioService");
                    }

                    if (usuarioDTO.getRole() != null) {
                        usuario.setRole(usuarioDTO.getRole());
                    }

                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    @Transactional
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado com o ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public Optional<UsuarioModel> buscarUsuario(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<UsuarioModel> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<UsuarioModel> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<UsuarioModel> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    private boolean validarEmail(String email) {
        return email != null && email.endsWith("@gmail.com");
    }

    private String gerarUsername(String email) {
        return email.split("@")[0];
    }

    public boolean isAdmin(UsuarioModel usuario) {
        return usuario != null && "ROLE_ADMIN".equals(usuario.getRole());
    }

    public boolean podeModificarUsuario(UsuarioModel usuarioAtual, Long usuarioAlvoId) {
        if (isAdmin(usuarioAtual)) {
            return true;
        }
        return usuarioAtual.getId().equals(usuarioAlvoId);
    }

    @Transactional
    public UsuarioModel atualizarPerfil(Long id, UsuarioDto usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioDTO.getNome());
                    usuario.setSobrenome(usuarioDTO.getSobrenome() != null ? usuarioDTO.getSobrenome() : usuario.getSobrenome());
                    usuario.setEmail(usuarioDTO.getEmail());

                    if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
                        System.out.println("Senha vazia, mensagem do UsuarioService");
                    }

                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
}