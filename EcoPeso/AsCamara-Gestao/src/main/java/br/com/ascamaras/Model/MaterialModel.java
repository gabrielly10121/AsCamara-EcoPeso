package br.com.ascamaras.Model;

import jakarta.persistence.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "material")
public class MaterialModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 50)
    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Min(value = 0, message = "Quantidade não pode ser negativa")
    @Column(nullable = false)
    private Integer quantidade = 0;

    @Min(value = 0, message = "Preço não pode ser negativo")
    @Column(name = "preco_por_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoPorKg;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "criado_por")
    private UsuarioModel criadoPor;

    @ManyToOne
    @JoinColumn(name = "atualizado_por")
    private UsuarioModel atualizadoPor;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PesagemModel> pesagens;



    public MaterialModel(){}

    public MaterialModel(Long id, String nome, String tipo, String descricao, Integer quantidade, BigDecimal precoPorKg, Boolean ativo, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, UsuarioModel criadoPor, UsuarioModel atualizadoPor, List<PesagemModel> pesagens) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.precoPorKg = precoPorKg;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.criadoPor = criadoPor;
        this.atualizadoPor = atualizadoPor;
        this.pesagens = pesagens;


    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoPorKg() {
        return precoPorKg;
    }

    public void setPrecoPorKg(BigDecimal precoPorKg) {
        this.precoPorKg = precoPorKg;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public UsuarioModel getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(UsuarioModel criadoPor) {
        this.criadoPor = criadoPor;
    }

    public UsuarioModel getAtualizadoPor() {
        return atualizadoPor;
    }

    public void setAtualizadoPor(UsuarioModel atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }

    public List<PesagemModel> getPesagens() {
        return pesagens;
    }

    public void setPesagens(List<PesagemModel> pesagens) {
        this.pesagens = pesagens;
    }


}