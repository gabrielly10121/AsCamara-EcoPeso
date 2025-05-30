package br.com.ascamaras.Model;

import jakarta.persistence.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pesagem")
public class PesagemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    @NotNull(message = "Material é obrigatório")
    private MaterialModel material;

    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = "Peso deve ser maior que zero")
    private Float peso;

    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "Valor unitário deve ser maior que zero")
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "Valor total deve ser maior que zero")
    private BigDecimal valorTotal;

    @Column(name = "data_pesagem", nullable = false)
    private OffsetDateTime dataPesagem;

    @Column(name = "total_material_pesado")
    private Integer totalMaterialPesado;

    @Column(name = "pesagem_realizadas")
    private Integer pesagemRealizadas;

    public void calcularValorTotal() {
        if (peso != null && valorUnitario != null) {
            this.valorTotal = valorUnitario.multiply(new BigDecimal(peso.toString()))
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }

    public PesagemModel(){}

    public PesagemModel(Long id, MaterialModel material, Float peso, BigDecimal valorUnitario, BigDecimal valorTotal, OffsetDateTime dataPesagem, Integer totalMaterialPesado, Integer pesagemRealizadas) {
        this.id = id;
        this.material = material;
        this.peso = peso;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.dataPesagem = dataPesagem;
        this.totalMaterialPesado = totalMaterialPesado;
        this.pesagemRealizadas = pesagemRealizadas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MaterialModel getMaterial() {
        return material;
    }

    public void setMaterial(MaterialModel material) {
        this.material = material;
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

    public OffsetDateTime getDataPesagem() {
        return dataPesagem;
    }

    public void setDataPesagem(OffsetDateTime dataPesagem) {
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