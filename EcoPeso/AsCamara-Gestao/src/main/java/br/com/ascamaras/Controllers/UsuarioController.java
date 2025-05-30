package br.com.ascamaras.Controllers;

import br.com.ascamaras.Dtos.UsuarioDto;
import br.com.ascamaras.Model.UsuarioModel;
import br.com.ascamaras.Repositories.UsuarioRepository;
import br.com.ascamaras.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller

public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
    }




    @GetMapping("/login")
    public String showLoginForm(Model model,
                                @RequestParam(value = "logout", required = false) String logout,
                                @RequestParam(value = "error", required = false) String error) {

        if (logout != null) {
            model.addAttribute("message", "Logout realizado com sucesso!");
        }

        if (error != null) {
            model.addAttribute("error", "Credenciais inválidas!");
        }

        model.addAttribute("usuario", new UsuarioModel());
        return "login";
    }


    @PostMapping("/usuario/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String senha,
                               RedirectAttributes redirectAttributes) {

        System.out.println("🔍 Tentativa de login para: " + email);

        if (email == null || email.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email é obrigatório!");
            return "redirect:/login";
        }

        if (senha == null || senha.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Senha é obrigatória!");
            return "redirect:/login";
        }

        Optional<UsuarioModel> usuarioOptional = usuarioService.buscarPorEmail(email);

        if (usuarioOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuário não encontrado!");
            return "redirect:/login";
        }

        UsuarioModel usuario = usuarioOptional.get();


        if (!usuario.getSenha().equals(senha)) {
            redirectAttributes.addFlashAttribute("error", "Senha incorreta!");
            return "redirect:/login";
        }

        return "redirect:/menu";
    }


    @GetMapping("/cadastro")
    public String showCadastroForm(Model model) {
        model.addAttribute("usuarioDto", new UsuarioDto());
        return "cadastro_usuario";
    }


    @PostMapping("/cadastro")
    public String processCadastro(@ModelAttribute UsuarioDto usuarioDto,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (!usuarioDto.getEmail().endsWith("@gmail.com")) {
                throw new IllegalArgumentException("O email deve terminar com @gmail.com");
            }

            if (!usuarioDto.getSenha().equals(usuarioDto.getConfirmarSenha())) {
                throw new IllegalArgumentException("As senhas não coincidem");
            }

            UsuarioModel usuario = usuarioService.criarUsuario(usuarioDto);
            redirectAttributes.addFlashAttribute("success", "Cadastro realizado com sucesso! Faça login.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuarioDto", usuarioDto);
            return "cadastro_usuario";
        }
    }


    @GetMapping("/perfil/editar")
    public String editarPerfilForm(Model model) {

        return "editar_perfil";
    }

    @PostMapping("/perfil/editar")
    public String atualizarPerfil(@ModelAttribute UsuarioModel usuario,
                                  RedirectAttributes redirectAttributes) {
        try {

            redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/perfil/editar";
    }



    @PostMapping("/api/usuarios")
    @ResponseBody
    public ResponseEntity<UsuarioModel> criarUsuarioApi(@RequestBody UsuarioDto usuarioDTO) {
        try {
            UsuarioModel usuario = usuarioService.criarUsuario(usuarioDTO);
            return new ResponseEntity<>(usuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<UsuarioModel> atualizarUsuarioApi(@PathVariable Long id,
                                                            @RequestBody UsuarioDto usuarioDTO) {
        try {
            UsuarioModel usuario = usuarioService.atualizarUsuario(id, usuarioDTO);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarUsuarioApi(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<UsuarioModel> buscarUsuarioApi(@PathVariable Long id) {
        Optional<UsuarioModel> usuario = usuarioService.buscarUsuario(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/api/usuarios")
    @ResponseBody
    public ResponseEntity<Iterable<UsuarioModel>> listarUsuariosApi() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }


    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Logout realizado com sucesso!");
        return "redirect:/login?logout=true";
    }



    @GetMapping("/error")
    public String handleError() {
        return "redirect:/login";
    }
}