package br.com.zupacademy.henriquecesar.propostas.controller;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.henriquecesar.propostas.client.analise_financeira.AnaliseFinanceiraClient;
import br.com.zupacademy.henriquecesar.propostas.dto.request.NovaPropostaRequest;
import br.com.zupacademy.henriquecesar.propostas.dto.response.PropostaStatusResponse;
import br.com.zupacademy.henriquecesar.propostas.exception.business.PropostaJaExisteException;
import br.com.zupacademy.henriquecesar.propostas.exception.business.PropostaNaoEncontradaException;
import br.com.zupacademy.henriquecesar.propostas.modelo.Proposta;
import br.com.zupacademy.henriquecesar.propostas.repository.PropostaRepository;

@RestController
@RequestMapping("/propostas")
public class PropostaController {
	
	private PropostaRepository propostaRepository;
	private AnaliseFinanceiraClient analiseFinanceiraClient;
	
	public PropostaController(PropostaRepository propostaRepository, AnaliseFinanceiraClient analiseFinanceiraClient) {
		this.propostaRepository = propostaRepository;
		this.analiseFinanceiraClient = analiseFinanceiraClient;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> criarProposta(@RequestBody @Valid NovaPropostaRequest request,
			UriComponentsBuilder uriBuilder) {
		Proposta proposta = Proposta.buildProposta(request);
		
		if (proposta.existeProposta(propostaRepository)) {
		    throw new PropostaJaExisteException();
		}
		
		propostaRepository.save(proposta);
		
		proposta.realizaAnaliseFinanceira(analiseFinanceiraClient, propostaRepository);
		
		URI location = uriBuilder.replacePath("/propostas/{id}")
				.buildAndExpand(proposta.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/{idProposta}")
	public PropostaStatusResponse consultarProposta(@PathVariable Long idProposta) {
		Proposta proposta = propostaRepository.findById(idProposta)
				.orElseThrow(PropostaNaoEncontradaException::new);
		
		return new PropostaStatusResponse(proposta);
	}

}
