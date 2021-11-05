package br.com.zupacademy.henriquecesar.propostas.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.SistemaCartoesClient;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovaInclusaoCarteiraResponse;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovaInclusaoCarteiraResponseStatus;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoResponse;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoResponseDiaVencimento;
import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;
import br.com.zupacademy.henriquecesar.propostas.modelo.CarteiraDigitalServico;
import br.com.zupacademy.henriquecesar.propostas.modelo.Proposta;
import br.com.zupacademy.henriquecesar.propostas.repository.CartaoRepository;
import br.com.zupacademy.henriquecesar.propostas.repository.PropostaRepository;
import feign.FeignException;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureDataJpa
@TestInstance(Lifecycle.PER_CLASS)
public class CarteiraDigitalControllerTest {
	
	private final String URI = "/carteiras";
	
	@MockBean
	private SistemaCartoesClient cartoesClient;
	
	@MockBean
	private PropostaRepository propostaRepository;
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Cartao cartaoAtual;
	
	@BeforeEach
	private void criarUmCartao() {
		NovoCartaoResponse cartaoResponse = new NovoCartaoResponse(
				"1234-5679-9101-1121", LocalDateTime.now(),
				"Nome Titular", new NovoCartaoResponseDiaVencimento(1),
				new BigDecimal("850.00"), "1");
			
		cartaoAtual = Cartao.buildCartao(cartaoResponse);
		cartaoRepository.save(cartaoAtual);
	}
	
	private Optional<Proposta> getPropostaMock() {
		return Optional.of(new Proposta("01234567890", "maria@teste.com", "Maria Teste",
				"Rua Teste, 505", new BigDecimal("1234100")));
	}
	
	private NovaInclusaoCarteiraResponse getRespostaBemSucedida() {
		return new NovaInclusaoCarteiraResponse(NovaInclusaoCarteiraResponseStatus.ASSOCIADA);
	}
	
	private void cadastraCarteiraNoBanco(CarteiraDigitalServico servico) {
		cartaoAtual.associarCarteira(servico, cartaoRepository);
	}
	
	private String getBaseUrlByServico(CarteiraDigitalServico servico) {
		switch (servico) {
		case PAYPAL:
			return "/%s/paypal";
		case SAMSUNG_PAY:
			return "/%s/samsung-pay";
		default:
			return "";
		}
	}
	
	@ParameterizedTest
	@EnumSource(CarteiraDigitalServico.class)
	void deveAssociarUmaCarteiraDigitalValida(CarteiraDigitalServico servico) throws Exception {
		when(cartoesClient.associarCarteira(Mockito.any(), Mockito.any()))
			.thenReturn(getRespostaBemSucedida());
		
		when(propostaRepository.findByCartao(Mockito.any()))
			.thenReturn(getPropostaMock());
		
		MockHttpServletRequestBuilder request = 
				post(URI.concat(String.format(getBaseUrlByServico(servico), cartaoAtual.getId())))
					.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(request).andExpect(status().isCreated())
			.andExpect(header().exists("Location"))
			.andExpect(redirectedUrlPattern(String
					.format("**/%s/carteiras/{spring:[0-9]+}", cartaoAtual.getId())
		));
	}

	@ParameterizedTest
	@EnumSource(CarteiraDigitalServico.class)
	void naoDeveAssociarUmaCarteiraDigitalDuplicada(CarteiraDigitalServico servico) throws Exception {
		when(cartoesClient.associarCarteira(Mockito.any(), Mockito.any()))
			.thenReturn(getRespostaBemSucedida());
	
		when(propostaRepository.findByCartao(Mockito.any()))
			.thenReturn(getPropostaMock());
		
		cadastraCarteiraNoBanco(servico);
		
		MockHttpServletRequestBuilder request = 
				post(URI.concat(String.format(getBaseUrlByServico(servico), cartaoAtual.getId())))
					.contentType(MediaType.APPLICATION_JSON);
	
		mockMvc.perform(request).andExpect(status().isUnprocessableEntity());
	}


	@Test
	void naoDeveAssociarUmaCarteiraDigitalSeRetornarFalhaDoSistemaLegado() throws Exception {
		FeignException.UnprocessableEntity exception = Mockito.mock(FeignException.UnprocessableEntity.class);
		
		when(cartoesClient.associarCarteira(Mockito.any(), Mockito.any()))
			.thenThrow(exception);

		when(propostaRepository.findByCartao(Mockito.any()))
			.thenReturn(getPropostaMock());
		
		MockHttpServletRequestBuilder request = 
				post(URI.concat(String.format("/%s/paypal", cartaoAtual.getId())))
					.contentType(MediaType.APPLICATION_JSON);
	
		mockMvc.perform(request).andExpect(status().isUnprocessableEntity());		
	}

}
