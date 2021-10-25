package br.com.zupacademy.henriquecesar.propostas.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zupacademy.henriquecesar.propostas.dto.request.NovaPropostaRequest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@TestInstance(Lifecycle.PER_CLASS)
public class PropostaControllerTest {
	
    @Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	private void fazRequisicaoEEsperaBadRequest(String json) throws Exception {
		URI uri = new URI("/propostas");
		mockMvc.perform(MockMvcRequestBuilders.post(uri)
			.content(json)
			.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status()
			.is(400));
	}

	@Test
	public void deveCadastrarUmaProposta() throws Exception {
		NovaPropostaRequest request = new NovaPropostaRequest(
			"01234567890",
			"email@exemplo.com",
			"Rua Exemplo 103",
			new BigDecimal(2500)
		);
		String requestJson = mapper.writeValueAsString(request);
		
		URI uri = new URI("/propostas");
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.content(requestJson)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status()
				.is(201))
		.andReturn();
		
		assertFalse(mvcResult.getResponse().getHeader("location").isEmpty());
	}
	
	@Test
	public void deveRetornarBadRequestComDocumentoInvalido() throws Exception {
		NovaPropostaRequest request = new NovaPropostaRequest(
			"00000000000",
			"email@exemplo.com",
			"Rua Exemplo 103",
			new BigDecimal(2500)
		);
		String requestJson = mapper.writeValueAsString(request);
		fazRequisicaoEEsperaBadRequest(requestJson);
	}
	
	@Test
	public void deveRetornarBadRequestComEmailInvalido() throws Exception {
		NovaPropostaRequest request = new NovaPropostaRequest(
			"01234567890",
			"emailinvalido.com",
			"Rua Exemplo 103",
			new BigDecimal(2500)
		);
		String requestJson = mapper.writeValueAsString(request);
		fazRequisicaoEEsperaBadRequest(requestJson);
	}
	
	@Test
	public void deveRetornarBadRequestComEnderecoEmBranco() throws Exception {
		NovaPropostaRequest request = new NovaPropostaRequest(
			"01234567890",
			"email@exemplo.com",
			"",
			new BigDecimal(2500)
		);
		String requestJson = mapper.writeValueAsString(request);
		fazRequisicaoEEsperaBadRequest(requestJson);
	}
	
	@Test
	public void deveRetornarBadRequestComSalarioInvalido() throws Exception {
		NovaPropostaRequest request = new NovaPropostaRequest(
			"01234567890",
			"email@exemplo.com",
			"Rua Exemplo 103",
			new BigDecimal(-2500)
		);
		String requestJson = mapper.writeValueAsString(request);
		fazRequisicaoEEsperaBadRequest(requestJson);
	}

}
