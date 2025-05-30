package br.com.ascamaras.Controllers;

import br.com.ascamaras.Dtos.UsuarioDto;
import br.com.ascamaras.Dtos.UsuarioLoginDto;
import br.com.ascamaras.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GerenciarUsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/gerenciar_usuario")
    public String gerenciarUsuario(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "gerenciar_usuario";
    }


    @GetMapping("/gerenciar_usuario/adicionar_usuario")
    public String adicionarUsuarioForm(Model model) {
        model.addAttribute("usuarioDto", new UsuarioDto());
        return "adicionar_usuario";
    }


    @PostMapping("/gerenciar_usuario/adicionar_usuario")
    public String adicionarUsuario(@ModelAttribute UsuarioDto usuarioDto,
                                   RedirectAttributes redirectAttributes) {
        try {
            usuarioService.criarUsuario(usuarioDto);
            redirectAttributes.addFlashAttribute("success", "Usuário cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/gerenciar_usuario/adicionar_usuario";
        }
        return "redirect:/gerenciar_usuario";
    }


    @GetMapping("/gerenciar_usuario/editar_usuario/{id}")
    public String editarUsuarioForm(@PathVariable Long id, Model model) {
        usuarioService.buscarUsuario(id).ifPresent(usuario -> {
            UsuarioDto usuarioDto = new UsuarioDto();
            usuarioDto.setId(usuario.getId());
            usuarioDto.setNome(usuario.getNome());
            usuarioDto.setSobrenome(usuario.getSobrenome());
            usuarioDto.setEmail(usuario.getEmail());
            usuarioDto.setUsername(usuario.getUsername());
            usuarioDto.setRole(usuario.getRole());
            model.addAttribute("usuarioDto", usuarioDto);
            model.addAttribute("usuarioId", id);
        });
        return "editar_usuario";
    }



    @PostMapping("/gerenciar_usuario/editar_usuario/{id}")
    public String editarUsuario(@PathVariable Long id,
                                @ModelAttribute UsuarioDto usuarioDto,
                                RedirectAttributes redirectAttributes) {
        try {
            usuarioService.atualizarUsuario(id, usuarioDto);
            redirectAttributes.addFlashAttribute("success", "Usuário atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/gerenciar_usuario";
    }


    @PostMapping("/gerenciar_usuario/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletarUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuário excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/gerenciar_usuario";
    }
}
