package br.com.ascamaras.Services;

import br.com.ascamaras.Model.MaterialModel;
import br.com.ascamaras.Repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public MaterialModel salvarMaterial(MaterialModel material) {
        return materialRepository.save(material);
    }

    @Transactional(readOnly = true)
    public List<MaterialModel> listarMateriais() {
        return materialRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MaterialModel> buscarMaterialPorId(Long id) {
        return materialRepository.findById(id);
    }

    public void excluirMaterial(Long id) {
        materialRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MaterialModel> buscarPorNome(String nome) {
        return materialRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<MaterialModel> buscarPorTipo(String tipo) {
        return materialRepository.findByTipo(tipo);
    }

    @Transactional(readOnly = true)
    public List<MaterialModel> buscarPorPrecoMinimo(BigDecimal precoMin) {
        return materialRepository.findByPrecoPorKgGreaterThanEqual(precoMin);
    }

    @Transactional(readOnly = true)
    public List<MaterialModel> buscarPorIntervaloPreco(BigDecimal precoMin, BigDecimal precoMax) {
        return materialRepository.findByPrecoPorKgBetween(precoMin, precoMax);
    }

    public List<String> listarNomesMateriais() {
        return materialRepository.findAll().stream()
                .map(MaterialModel::getNome)
                .distinct()
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<MaterialModel> listarMateriais(String nome, String tipo, BigDecimal precoMin, BigDecimal precoMax) {
        if (nome != null && tipo != null && precoMin != null && precoMax != null) {
            return materialRepository.findByNomeContainingIgnoreCaseAndTipoAndPrecoPorKgBetween(nome, tipo, precoMin, precoMax);
        } else if (nome != null && tipo != null) {
            return materialRepository.findByNomeContainingIgnoreCaseAndTipo(nome, tipo);
        } else if (precoMin != null && precoMax != null) {
            return materialRepository.findByPrecoPorKgBetween(precoMin, precoMax);
        } else if (nome != null) {
            return materialRepository.findByNomeContainingIgnoreCase(nome);
        } else if (tipo != null) {
            return materialRepository.findByTipo(tipo);
        } else if (precoMin != null) {
            return materialRepository.findByPrecoPorKgGreaterThanEqual(precoMin);
        } else if (precoMax != null) {
            return materialRepository.findByPrecoPorKgLessThanEqual(precoMax);
        } else {
            return materialRepository.findAll();
        }
    }
}