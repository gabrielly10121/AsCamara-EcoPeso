package br.com.ascamaras.Controllers;

import br.com.ascamaras.Model.MaterialModel;
import br.com.ascamaras.Services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/materiais")
public class MaterialController {

    private final MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/cadastro")
    public String exibirCadastroMaterial(Model model) {
        model.addAttribute("material", new MaterialModel());
        model.addAttribute("materiais", materialService.listarMateriais());
        return "cadastro_material";
    }

    @PostMapping("/cadastrar")
    public String cadastrarMaterial(@ModelAttribute MaterialModel material,
                                    RedirectAttributes redirectAttributes) {
        materialService.salvarMaterial(material);
        redirectAttributes.addFlashAttribute("mensagem", "Material cadastrado com sucesso!");
        return "redirect:/materiais/cadastro";
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<MaterialModel>> listarMateriais(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "precoMin", required = false) BigDecimal precoMin,
            @RequestParam(value = "precoMax", required = false) BigDecimal precoMax) {

        List<MaterialModel> materiais = materialService.listarMateriais(nome, tipo, precoMin, precoMax);
        return new ResponseEntity<>(materiais, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<MaterialModel> criarMaterial(@Valid @RequestBody MaterialModel material) {
        MaterialModel savedMaterial = materialService.salvarMaterial(material);
        return new ResponseEntity<>(savedMaterial, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<MaterialModel> atualizarMaterial(@PathVariable Long id,
                                                           @RequestBody MaterialModel material) {
        material.setId(id);
        MaterialModel updatedMaterial = materialService.salvarMaterial(material);
        return new ResponseEntity<>(updatedMaterial, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarMaterial(@PathVariable Long id) {
        materialService.excluirMaterial(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}