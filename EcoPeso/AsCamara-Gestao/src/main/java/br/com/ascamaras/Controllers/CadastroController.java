package br.com.ascamaras.Controllers;

import br.com.ascamaras.Dtos.UsuarioDto;
import br.com.ascamaras.Model.UsuarioModel;
import br.com.ascamaras.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cadastro")
public class CadastroController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuario")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuarioDto", new UsuarioDto());
        return "cadastro_usuario";
    }

    @PostMapping("/usuario")
    public String registerUser(@ModelAttribute UsuarioDto usuarioDto,
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

    @GetMapping("/sucesso")
    public String cadastroSucesso() {
        return "cadastro_sucesso";
    }
}