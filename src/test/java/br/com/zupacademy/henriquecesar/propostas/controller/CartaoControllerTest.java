package br.com.zupacademy.henriquecesar.propostas.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoResponse;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoResponseDiaVencimento;
import br.com.zupacademy.henriquecesar.propostas.dto.request.NovaBiometriaRequest;
import br.com.zupacademy.henriquecesar.propostas.dto.request.NovoAvisoViagemRequest;
import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;
import br.com.zupacademy.henriquecesar.propostas.repository.CartaoRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureDataJpa
@TestInstance(Lifecycle.PER_CLASS)
class CartaoControllerTest {

	private final String BASE64 = "SGVsbG8gT3JhbmdlVGFsZW50cyE=";
	private final String URI = "/cartoes";
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	private Cartao cartaoAtual;
	
	@BeforeEach
	private void beforeEach() {
		NovoCartaoResponse cartaoResponse = new NovoCartaoResponse(
				"1234-5679-9101-1121", LocalDateTime.now(),
				"Nome Titular", new NovoCartaoResponseDiaVencimento(1),
				new BigDecimal("850.00"), "1");
			
		cartaoAtual = Cartao.buildCartao(cartaoResponse);
		cartaoRepository.save(cartaoAtual);
	}

	@Test
	void deveCadastrarBiometria() throws Exception {
		NovaBiometriaRequest body = new NovaBiometriaRequest(BASE64);
		
		MockHttpServletRequestBuilder request = 
			post(URI.concat(String.format("/%s/adicionarBiometria", cartaoAtual.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body));
		
		mvc.perform(request).andExpect(status().isCreated())
			.andExpect(header().exists("Location"))
			.andExpect(redirectedUrlPattern(String
				.format("**/%s/{spring:[0-9]+}", cartaoAtual.getId())
			)
		);
	}
	
	@Test
	void naoDeveCadastrarBiometriaEmCartaoInexistente() throws Exception {
		NovaBiometriaRequest body = new NovaBiometriaRequest(BASE64);
		String idInexistente = "2073789c-efef-450f-b8b1-2d7149d57f85";
		
		MockHttpServletRequestBuilder request = 
			post(URI.concat(String.format("/%s/adicionarBiometria", idInexistente)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body));
			
		mvc.perform(request).andExpect(status().isNotFound());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	void naoDeveCadastrarBiometriaComFingerprintInvalida(String fingerPrint) throws Exception {
		NovaBiometriaRequest body = new NovaBiometriaRequest(fingerPrint);
		
		MockHttpServletRequestBuilder request = 
			post(URI.concat(String.format("/%s/adicionarBiometria", cartaoAtual.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body));
			
		mvc.perform(request).andExpect(status().isBadRequest());
	}
	
	@Test
	void deveBloquearCartao() throws Exception {
		MockHttpServletRequestBuilder request = 
				post(URI.concat(String.format("/%s/bloquear", cartaoAtual.getId())))
					.header("User-Agent", "teste")
					.contentType(MediaType.APPLICATION_JSON);
			
		mvc.perform(request).andExpect(status().isOk());
	}
	
	@Test
	void naoDeveBloquearCartaoInexistente() throws Exception {
		String idInexistente = "2073789c-efef-450f-b8b1-2d7149d57f85";
		
		MockHttpServletRequestBuilder request = 
				post(URI.concat(String.format("/%s/bloquear", idInexistente)))
					.contentType(MediaType.APPLICATION_JSON);
			
		mvc.perform(request).andExpect(status().isNotFound());
	}
	
	void naoDeveBloquearCartaoJaBloqueado() throws Exception {
		deveBloquearCartao();
		
		MockHttpServletRequestBuilder request = 
				post(URI.concat(String.format("/%s/bloquear", cartaoAtual.getId())))
					.contentType(MediaType.APPLICATION_JSON);
			
		mvc.perform(request).andExpect(status().isUnprocessableEntity());
	}
	
	
	@Test
	void deveCadastrarAvisoViagem() throws Exception {
		NovoAvisoViagemRequest body = new NovoAvisoViagemRequest("Recife", LocalDateTime.now().plusDays(10L));
		
		MockHttpServletRequestBuilder request = 
			post(URI.concat(String.format("/%s/avisoViagem", cartaoAtual.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.header("User-Agent", "Test")
				.content(new ObjectMapper()
						.findAndRegisterModules()
						.writeValueAsString(body));
		
		mvc.perform(request).andExpect(status().isOk());
	}
	
	@Test
	void naoDeveCadastrarAvisoViagemEmCartaoInexistente() throws Exception {
		NovoAvisoViagemRequest body = new NovoAvisoViagemRequest("Recife", LocalDateTime.now().plusDays(10L));
		String idInexistente = "2073789c-efef-450f-b8b1-2d7149d57f85";
		
		MockHttpServletRequestBuilder request = 
			post(URI.concat(String.format("/%s/avisoViagem", idInexistente)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper()
						.findAndRegisterModules()
						.writeValueAsString(body));
			
		mvc.perform(request).andExpect(status().isNotFound());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	void naoDeveCadastrarAvisoViagemComDestinoInvalido(String destino) throws Exception {
		NovoAvisoViagemRequest body = new NovoAvisoViagemRequest(destino, LocalDateTime.now().plusDays(10L));
		
		MockHttpServletRequestBuilder request = 
			post(URI.concat(String.format("/%s/avisoViagem", cartaoAtual.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper()
						.findAndRegisterModules()
						.writeValueAsString(body));
			
		mvc.perform(request).andExpect(status().isBadRequest());
	}
	
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"2017-03-14T12:34:56.789"})
	void naoDeveCadastrarAvisoViagemComDataInvalida(LocalDateTime data) throws Exception {
		NovoAvisoViagemRequest body = new NovoAvisoViagemRequest("Recife", data);

		
		MockHttpServletRequestBuilder request = 
			post(URI.concat(String.format("/%s/avisoViagem", cartaoAtual.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper()
						.findAndRegisterModules()
						.writeValueAsString(body));
			
		mvc.perform(request).andExpect(status().isBadRequest());
	}

}
