package br.com.ascamaras.Controllers;

import br.com.ascamaras.Model.MaterialModel;
import br.com.ascamaras.Model.PesagemModel;
import br.com.ascamaras.Services.MaterialService;
import br.com.ascamaras.Services.PesagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;


@Controller
@RequestMapping("/pesagem")
public class PesagemController {

    @Autowired
    private PesagemService pesagemService;

    @Autowired
    private MaterialService materialService;


    @GetMapping("/menu")
    public String exibirMenu() {
        return "menu";
    }


    @GetMapping("/")
    public String redirecionarParaMenu() {
        return "redirect:/menu";
    }

    @GetMapping
    public String exibirFormularioPesagem(Model model) {
        model.addAttribute("pesagem", new PesagemModel());
        model.addAttribute("materiais", materialService.listarMateriais());
        return "pesagem";
    }

    @PostMapping
    public String processarPesagem(@RequestParam("materialId") Long materialId,
                                   @Valid @ModelAttribute PesagemModel pesagem,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.pesagem", result);
            redirectAttributes.addFlashAttribute("pesagem", pesagem);
            return "redirect:/pesagem";
        }

        try {

            MaterialModel material = materialService.buscarMaterialPorId(materialId)
                    .orElseThrow(() -> new IllegalArgumentException("Material não encontrado"));

            pesagem.setMaterial(material);
            validarConsistenciaPesagem(pesagem);
            configurarDataPadrao(pesagem);

            pesagemService.salvarPesagem(pesagem);
            redirectAttributes.addFlashAttribute("mensagem", "Pesagem registrada com sucesso!");
            return "redirect:/pesagem/historico";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/pesagem";
        }
    }

    @GetMapping("/historico")
    public String exibirHistorico(Model model) {
        model.addAttribute("pesagens", pesagemService.listarPesagens());
        return "historico_pesagem";
    }

    private void validarConsistenciaPesagem(PesagemModel pesagem) {
        BigDecimal valorCalculado = pesagem.getValorUnitario()
                .multiply(new BigDecimal(pesagem.getPeso().toString()))
                .setScale(2, RoundingMode.HALF_UP);

        if (pesagem.getValorTotal().compareTo(valorCalculado) != 0) {
            throw new IllegalArgumentException("Valor total inconsistente com os valores informados");
        }
    }

    private void configurarDataPadrao(PesagemModel pesagem) {
        if (pesagem.getDataPesagem() == null) {
            pesagem.setDataPesagem(OffsetDateTime.now());
        }
    }
}