package com.project.pontointeligente.api.centroCusto;

import com.project.pontointeligente.api.empresa.Empresa;
import com.project.pontointeligente.api.empresa.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CentroCustoServiceImpl implements CentroCustoService {

    private CentroCustoRepository repository;
    private EmpresaService empresaService;

    @Autowired
    public CentroCustoServiceImpl(CentroCustoRepository repository, EmpresaService empresaService) {
        this.repository = repository;
        this.empresaService = empresaService;
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
    public void excluir(CentroCusto centroCusto) {
        repository.delete(centroCusto);
    }

    @Override
    public List<CentroCusto> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<CentroCusto> buscarPorId(Long id) {
        return Optional.ofNullable(repository.findOne(id));
    }
}
