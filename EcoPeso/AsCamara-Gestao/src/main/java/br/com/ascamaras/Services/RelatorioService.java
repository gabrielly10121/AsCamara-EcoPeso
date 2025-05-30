package br.com.ascamaras.Services;

import br.com.ascamaras.Model.MaterialModel;
import br.com.ascamaras.Model.PesagemModel;
import br.com.ascamaras.Repositories.MaterialRepository;
import br.com.ascamaras.Repositories.PesagemRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private final MaterialRepository materialRepository;
    private final PesagemRepository pesagemRepository;

    @Autowired
    public RelatorioService(MaterialRepository materialRepository, PesagemRepository pesagemRepository) {
        this.materialRepository = materialRepository;
        this.pesagemRepository = pesagemRepository;
    }


    public File gerarRelatorio(OffsetDateTime dataInicio, OffsetDateTime dataFim, String tipoMaterial, String formato) throws IOException {
        try {
            String nomeArquivo = String.format("relatorio_%s_%s_%s.%s",
                    tipoMaterial.replace(" ", "_"),
                    dataInicio.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    dataFim.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    formato.equalsIgnoreCase("pdf") ? "pdf" : "xlsx"
            );

            String caminho = System.getProperty("java.io.tmpdir") + File.separator + nomeArquivo;

            if (formato.equalsIgnoreCase("pdf")) {
                gerarRelatorioPDF(caminho, dataInicio, dataFim, tipoMaterial);
            } else if (formato.equalsIgnoreCase("excel")) {
                gerarRelatorioExcel(caminho, dataInicio, dataFim, tipoMaterial);
            } else {
                throw new IllegalArgumentException("Formato inválido: " + formato);
            }

            return new File(caminho);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Erro ao gerar relatório: " + e.getMessage());
        }
    }


    public void gerarRelatorioPDF(String filePath, OffsetDateTime dataInicio, OffsetDateTime dataFim, String tipoMaterial) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();


        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph titulo = new Paragraph("Relatório de Pesagem de Materiais", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        document.add(new Paragraph("Período: " + dataInicio.toLocalDate() + " a " + dataFim.toLocalDate()));
        document.add(new Paragraph("Material: " + tipoMaterial));
        document.add(new Paragraph(" "));


        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4, 4, 2});

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        String[] headers = {"Data", "Material", "Peso (kg)"};

        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setBackgroundColor(BaseColor.GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }


        List<PesagemModel> pesagens = pesagemRepository.findByDataPesagemBetweenAndMaterial_NomeContainingIgnoreCase(
                dataInicio, dataFim, tipoMaterial);

        for (PesagemModel pesagem : pesagens) {
            table.addCell(pesagem.getDataPesagem().toLocalDate().toString());
            table.addCell(pesagem.getMaterial().getNome());
            table.addCell(String.valueOf(pesagem.getPeso()));
        }

        document.add(table);
        document.close();
    }


    public void gerarRelatorioExcel(String filePath, OffsetDateTime dataInicio, OffsetDateTime dataFim, String tipoMaterial) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pesagens");


        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);

        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);


        Row header = sheet.createRow(0);
        String[] headers = {"Data", "Material", "Peso (kg)"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }


        List<PesagemModel> pesagens = pesagemRepository.findByDataPesagemBetweenAndMaterial_NomeContainingIgnoreCase(
                dataInicio, dataFim, tipoMaterial);

        int rowNum = 1;
        for (PesagemModel pesagem : pesagens) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pesagem.getDataPesagem().toLocalDate().toString());
            row.createCell(1).setCellValue(pesagem.getMaterial().getNome());
            row.createCell(2).setCellValue(pesagem.getPeso());
        }


        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }


    public List<String> listarTiposMateriais() {
        List<MaterialModel> materiais = materialRepository.findAll();
        return materiais.stream()
                .map(MaterialModel::getNome)
                .collect(Collectors.toList());
    }
}
