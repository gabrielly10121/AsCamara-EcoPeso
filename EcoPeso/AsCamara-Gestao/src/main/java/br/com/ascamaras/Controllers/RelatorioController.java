package br.com.ascamaras.Controllers;

import br.com.ascamaras.Services.RelatorioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@Controller
@RequestMapping("/relatorio")
public class RelatorioController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioController.class);
    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping
    public String exibirPaginaRelatorio(Model model) {
        List<String> tiposMateriais = relatorioService.listarTiposMateriais();
        LOGGER.info("Enviando para a view: {} tipos de materiais", tiposMateriais.size());
        model.addAttribute("tiposMateriais", tiposMateriais);
        return "relatorio";
    }

    @GetMapping("/formulario")
    public String formularioRelatorio() {
        return "formulario_relatorio";
    }

    @GetMapping("/historico")
    public String historicoRelatorios() {
        return "historico_relatorios";
    }

    @GetMapping("/gerar-relatorio")
    public ResponseEntity<InputStreamResource> gerarRelatorio(
            @RequestParam("dataInicial") String dataInicial,
            @RequestParam("dataFinal") String dataFinal,
            @RequestParam("tipoMaterial") String tipoMaterial,
            @RequestParam("formato") String formato) throws IOException {

        LOGGER.info("Gerando relatório - Início: {}, Fim: {}, Material: {}, Formato: {}",
                dataInicial, dataFinal, tipoMaterial, formato);

        OffsetDateTime inicio = OffsetDateTime.parse(dataInicial);
        OffsetDateTime fim = OffsetDateTime.parse(dataFinal);

        File file = relatorioService.gerarRelatorio(inicio, fim, tipoMaterial, formato);

        if (file == null || !file.exists()) {
            LOGGER.error("Arquivo de relatório não encontrado");
            throw new IOException("Falha ao gerar relatório");
        }

        MediaType mediaType = formato.equalsIgnoreCase("pdf") ?
                MediaType.APPLICATION_PDF :
                MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @GetMapping("/tipos-materiais")
    @ResponseBody
    public List<String> listarTiposMateriais() {
        List<String> tipos = relatorioService.listarTiposMateriais();
        LOGGER.info("Retornando {} tipos de materiais via API", tipos.size());
        return tipos;
    }
}
