package br.com.ascamaras.Controllers;

import br.com.ascamaras.Model.UsuarioModel;
import br.com.ascamaras.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String menu() {
        return "menu";
    }

    @GetMapping("/menu")
    public String exibirMenu() {
        return "menu";
    }
}
