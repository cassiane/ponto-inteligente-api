package com.project.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import com.project.pontointeligente.api.lancamento.LancamentoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.project.pontointeligente.api.PontoInteligenteApplication;
import com.project.pontointeligente.api.lancamento.Lancamento;
import com.project.pontointeligente.api.lancamento.LancamentoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PontoInteligenteApplication.class)
@ActiveProfiles("test")
public class LancamentoServiceRepositoryTest {

	@MockBean
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private LancamentoService lancamentoService;

	@Before
	public void setUp() throws Exception {
		BDDMockito
				.given(this.lancamentoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
				.willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
		BDDMockito.given(this.lancamentoRepository.findOne(Mockito.anyLong())).willReturn(new Lancamento());
		BDDMockito.given(this.lancamentoRepository.save(Mockito.any(Lancamento.class))).willReturn(new Lancamento());
	}

	@Test
	public void testBuscarPorFuncionarioIdPaginado() {
		Page<Lancamento> lancamento = this.lancamentoService.buscarPorFuncionarioId(1L, new PageRequest(0, 10));
		assertNotNull(lancamento);
	}

	@Test
	public void testBuscarPorFuncionarioId() {
		Optional<Lancamento> lancamento = this.lancamentoService.buscarLancamentoPorId(1L);
		assertTrue(lancamento.isPresent());
	}

	@Test
	public void testPersistir() {
		Lancamento lancamento = this.lancamentoService.persistirLancamento(new Lancamento());
		assertNotNull(lancamento);
	}

}
