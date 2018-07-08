package com.project.pontointeligente.api.centroCusto;

import com.project.pontointeligente.api.empresa.Empresa;
import com.project.pontointeligente.api.empresa.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CentroCustoServiceImpl implements CentroCustoService {

    private CentroCustoRepository repository;
    private EmpresaService empresaService;
    private CentroCustoToCentroCustoDto centroCustoToCentroCustoDto;

    @Autowired
    public CentroCustoServiceImpl(CentroCustoRepository repository, EmpresaService empresaService, CentroCustoToCentroCustoDto centroCustoToCentroCustoDto) {
        this.repository = repository;
        this.empresaService = empresaService;
        this.centroCustoToCentroCustoDto = centroCustoToCentroCustoDto;
    }

    @Override
    public CentroCusto persistir(CentroCusto centroCusto) {
        Optional<Empresa> empresa = empresaService.buscarPorCnpj(centroCusto.getEmpresa().getCnpj());
        if (empresa.isPresent()) {
            centroCusto.setEmpresa(empresa.get());
            return repository.save(centroCusto);
        }

        return null;
    }

    @Override
    public void excluir(Long id) throws Exception {
        CentroCusto centroCusto = repository.findOne(id);
        if (centroCusto != null) {
            repository.delete(centroCusto);
        } else {
            throw new Exception("Centro de custo não encontrado");
        }
    }

    @Override
    public List<CentroCustoDto> listar() {
        return repository.findAll().stream().map(centroCusto -> centroCustoToCentroCustoDto.convert(centroCusto)).collect(Collectors.toList());
    }

    @Override
    public Optional<CentroCustoDto> buscarPorId(Long id) {
        CentroCusto centroCusto = repository.findOne(id);
        if (centroCusto != null) {
            return Optional.of(centroCustoToCentroCustoDto.convert(centroCusto));
        } else {
            return Optional.empty();
        }
    }
}
