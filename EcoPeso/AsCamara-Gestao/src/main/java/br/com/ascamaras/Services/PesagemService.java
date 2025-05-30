package br.com.ascamaras.Services;

import br.com.ascamaras.Model.PesagemModel;
import br.com.ascamaras.Repositories.PesagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PesagemService {

    @Autowired
    private PesagemRepository pesagemRepository;

    @Transactional
    public PesagemModel salvarPesagem(PesagemModel pesagem) {

        pesagem.setValorTotal(pesagem.getValorTotal().setScale(2, RoundingMode.HALF_UP));
        pesagem.setValorUnitario(pesagem.getValorUnitario().setScale(2, RoundingMode.HALF_UP));


        if (pesagem.getDataPesagem() == null) {
            pesagem.setDataPesagem(OffsetDateTime.now());
        }

        return pesagemRepository.save(pesagem);
    }

    @Transactional(readOnly = true)
    public List<PesagemModel> listarPesagens() {
        return pesagemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PesagemModel> buscarPesagemPorId(Long id) {
        return pesagemRepository.findById(id);
    }

    @Transactional
    public void excluirPesagem(Long id) {
        pesagemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PesagemModel> buscarPesagensPorPeriodo(OffsetDateTime dataInicial, OffsetDateTime dataFinal) {
        return pesagemRepository.findByDataPesagemBetween(dataInicial, dataFinal);
    }

    @Transactional(readOnly = true)
    public List<PesagemModel> buscarPesagensPorPeriodoETipoMaterial(OffsetDateTime dataInicial,
                                                                    OffsetDateTime dataFinal,
                                                                    String tipoMaterial) {
        return pesagemRepository.findByDataPesagemBetweenAndMaterial_NomeContainingIgnoreCase(
                dataInicial, dataFinal, tipoMaterial);
    }

    @Transactional(readOnly = true)
    public List<PesagemModel> buscarPesagensPorPeso(Float pesoMin, Float pesoMax) {
        if (pesoMin != null && pesoMax != null) {
            return pesagemRepository.findByPesoBetween(pesoMin, pesoMax);
        } else if (pesoMin != null) {
            return pesagemRepository.findByPesoGreaterThanEqual(pesoMin);
        } else if (pesoMax != null) {
            return pesagemRepository.findByPesoLessThanEqual(pesoMax);
        } else {
            return pesagemRepository.findAll();
        }
    }


    @Transactional
    public PesagemModel calcularESalvarPesagem(PesagemModel pesagem) {
        pesagem.calcularValorTotal();
        return this.salvarPesagem(pesagem);
    }
}