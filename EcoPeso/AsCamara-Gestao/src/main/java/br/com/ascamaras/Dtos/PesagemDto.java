package br.com.ascamaras.Dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class PesagemDto {

    private Long tipoMaterial;
    private Float peso;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private String dataPesagem;
    private Integer totalMaterialPesado;
    private Integer pesagemRealizadas;


    public OffsetDateTime getDataPesagemAsOffsetDateTime() {
        return OffsetDateTime.parse(this.dataPesagem);
    }

    public Long getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(Long tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDataPesagem() {
        return dataPesagem;
    }

    public void setDataPesagem(String dataPesagem) {
        this.dataPesagem = dataPesagem;
    }

    public Integer getTotalMaterialPesado() {
        return totalMaterialPesado;
    }

    public void setTotalMaterialPesado(Integer totalMaterialPesado) {
        this.totalMaterialPesado = totalMaterialPesado;
    }

    public Integer getPesagemRealizadas() {
        return pesagemRealizadas;
    }

    public void setPesagemRealizadas(Integer pesagemRealizadas) {
        this.pesagemRealizadas = pesagemRealizadas;
    }
}
