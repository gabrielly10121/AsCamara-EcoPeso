package br.com.ascamaras.Repositories;

import br.com.ascamaras.Model.PesagemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface PesagemRepository extends JpaRepository<PesagemModel, Long> {


    List<PesagemModel> findByDataPesagemBetweenAndMaterial_NomeContainingIgnoreCase(
            OffsetDateTime dataInicial,
            OffsetDateTime dataFinal,
            String tipoMaterial);


    List<PesagemModel> findByDataPesagemBetween(
            OffsetDateTime dataInicial,
            OffsetDateTime dataFinal);


    List<PesagemModel> findByPesoBetween(Float pesoMin, Float pesoMax);


    List<PesagemModel> findByPesoGreaterThanEqual(Float pesoMin);


    List<PesagemModel> findByPesoLessThanEqual(Float pesoMax);
}
