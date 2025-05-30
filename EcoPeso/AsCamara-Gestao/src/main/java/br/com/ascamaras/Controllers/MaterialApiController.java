package br.com.ascamaras.Controllers;

import br.com.ascamaras.Model.MaterialModel;
import br.com.ascamaras.Services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/materiais")
public class MaterialApiController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/tipos")
    public List<String> listarTiposDeMateriais() {
        return materialService.listarMateriais().stream()
                .map(MaterialModel::getNome)
                .distinct()
                .toList();
    }

    @GetMapping
    public List<MaterialModel> listarTodosMateriais() {
        return materialService.listarMateriais();
    }
}