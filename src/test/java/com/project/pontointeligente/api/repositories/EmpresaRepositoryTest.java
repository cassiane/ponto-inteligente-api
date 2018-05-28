package com.project.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;

import com.project.pontointeligente.api.empresa.EmpresaRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.project.pontointeligente.api.empresa.Empresa;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {

	@Autowired
	private EmpresaRepository empresaRepository;

	private static final String cnpj = "51463645000100";

	@Before
	public void setUp() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de Exemplo");
		empresa.setCnpj(cnpj);
		empresaRepository.save(empresa);
	}
	
	@After
	public void tearDown(){
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarPorCnpj(){
		Empresa empresa = this.empresaRepository.findByCnpj(cnpj);
		assertEquals(cnpj,empresa.getCnpj());
	}
}
