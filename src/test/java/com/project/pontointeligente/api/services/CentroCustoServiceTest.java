package com.project.pontointeligente.api.services;

import com.project.pontointeligente.api.PontoInteligenteApplication;
import com.project.pontointeligente.api.centroCusto.CentroCusto;
import com.project.pontointeligente.api.centroCusto.CentroCustoRepository;
import com.project.pontointeligente.api.centroCusto.CentroCustoService;
import com.project.pontointeligente.api.empresa.Empresa;
import com.project.pontointeligente.api.empresa.EmpresaRepository;
import com.project.pontointeligente.api.empresa.EmpresaService;
import com.project.pontointeligente.api.lancamento.LancamentoLogRepository;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PontoInteligenteApplication.class)
@ActiveProfiles("test")
public class CentroCustoServiceTest {

    @Autowired
    private CentroCustoService centroCustoService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CentroCustoRepository repository;

    @Autowired
    private LancamentoLogRepository lancamentoLogRepository;

    private Empresa empresa;
    private CentroCusto centroCusto;

    @Before
    public void setUp() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setCnpj("123456");
        empresa.setRazaoSocial("Razao teste");

        this.empresa = empresaService.persistir(empresa);
    }

    @After
    public void tearDown() throws Exception {
        lancamentoLogRepository.deleteAll();
        empresaRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    public void persistir() {
        centroCusto = new CentroCusto();
        centroCusto.setCentroCusto(1);
        centroCusto.setProjeto("Projeto 1");
        centroCusto.setDescricao("teste");
        centroCusto.setEmpresa(empresa);
        centroCusto = centroCustoService.persistir(centroCusto);

        assertNotNull(centroCusto);
    }

    @Test
    public void excluir() {
    }

    @Test
    public void listar() {
    }
}