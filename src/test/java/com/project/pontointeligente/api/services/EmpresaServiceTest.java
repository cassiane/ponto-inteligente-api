package com.project.pontointeligente.api.services;

import static org.junit.Assert.*;

import java.util.Optional;

import com.project.pontointeligente.api.empresa.EmpresaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.project.pontointeligente.api.PontoInteligenteApplication;
import com.project.pontointeligente.api.empresa.Empresa;
import com.project.pontointeligente.api.empresa.EmpresaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PontoInteligenteApplication.class)
@ActiveProfiles("test")
public class EmpresaServiceTest {

	@MockBean
	private EmpresaRepository empresaRepository;

	@Autowired
	private EmpresaService empresaService;

	private static final String CNPJ = "51463645000100";

	@Before
	public void setUp() {
		BDDMockito.given(this.empresaRepository.findByCnpj(Mockito.anyString())).willReturn(new Empresa());
		BDDMockito.given(this.empresaRepository.save(Mockito.any(Empresa.class))).willReturn(new Empresa());
	}

	@Test
	public void testBuscarEmpresaPorCnpj() {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(CNPJ);
		assertTrue(empresa.isPresent());
	}

	@Test
	public void testPersistirEmpresa() {
		Empresa empresa = this.empresaService.persistir(new Empresa());
		assertNotNull(empresa);
	}
}
