package br.com.zupacademy.henriquecesar.propostas.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zupacademy.henriquecesar.propostas.client.analise_financeira.AnaliseFinanceiraClient;
import br.com.zupacademy.henriquecesar.propostas.client.analise_financeira.dto.NovaAnaliseFinanceiraRequest;
import br.com.zupacademy.henriquecesar.propostas.client.analise_financeira.dto.NovaAnaliseFinanceiraResponse;
import br.com.zupacademy.henriquecesar.propostas.common.annotation.CpfOrCnpj;
import br.com.zupacademy.henriquecesar.propostas.dto.request.NovaPropostaRequest;
import br.com.zupacademy.henriquecesar.propostas.repository.PropostaRepository;

@Entity
public class Proposta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CpfOrCnpj
	private String documento;

	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	private String nome;

	@NotBlank
	private String endereco;

	@NotNull
	private BigDecimal salario;
	
	@Enumerated(EnumType.STRING)
	private PropostaStatus status;

	@Deprecated
	public Proposta() {
	}

	public Proposta(String documento, @Email @NotBlank String email, @NotBlank String nome, 
			@NotBlank String endereco, @NotNull BigDecimal salario) {
		this.documento = documento;
		this.email = email;
		this.nome = nome;
		this.endereco = endereco;
		this.salario = salario;
	}

	public static Proposta buildProposta(@Valid NovaPropostaRequest request) {
		return new Proposta(
			request.getDocumento(),
			request.getEmail(),
			request.getNome(),
			request.getEndereco(),
			request.getSalario()
		);
	}

	public Long getId() {
		return id;
	}
	
	public String getDocumento() {
		return documento;
	}
	
	public String getNome() {
		return nome;
	};

    public boolean existeProposta(PropostaRepository propostaRepository) {
        return propostaRepository.findByDocumento(documento).isPresent();
    }

	public void realizaAnaliseFinanceira(AnaliseFinanceiraClient analiseFinanceiraClient, PropostaRepository repository) {
		NovaAnaliseFinanceiraResponse response = analiseFinanceiraClient
				.realizarAnaliseFinanceira(new NovaAnaliseFinanceiraRequest(this));
		status = response.getPropostaStatus();
		repository.save(this);
	}

}
