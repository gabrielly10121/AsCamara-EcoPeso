package br.com.ascamaras.Repositories;

import br.com.ascamaras.Model.MaterialModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<MaterialModel, Long> {
    List<MaterialModel> findByNomeContainingIgnoreCase(String nome);
    List<MaterialModel> findByTipo(String tipo);
    List<MaterialModel> findByPrecoPorKgGreaterThanEqual(BigDecimal preco);
    List<MaterialModel> findByPrecoPorKgLessThanEqual(BigDecimal preco);
    List<MaterialModel> findByPrecoPorKgBetween(BigDecimal precoInicial, BigDecimal precoFinal);
    List<MaterialModel> findByQuantidadeGreaterThanEqual(Integer quantidade);
    List<MaterialModel> findByNomeContainingIgnoreCaseAndTipo(String nome, String tipo);
    List<MaterialModel> findByNomeContainingIgnoreCaseAndTipoAndPrecoPorKgBetween(
            String nome, String tipo, BigDecimal precoMin, BigDecimal precoMax);

    @Query("SELECT DISTINCT m.tipo FROM MaterialModel m WHERE m.tipo IS NOT NULL AND m.tipo <> '' ORDER BY m.tipo")
    List<String> findDistinctTipos();
}