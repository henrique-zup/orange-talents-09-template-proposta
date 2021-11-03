package br.com.zupacademy.henriquecesar.propostas.modelo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class AvisoViagem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String destino;
	
	@NotNull
	@Future
	private LocalDateTime dataViagem;
	
	@ManyToOne(optional = false)
	private Cartao cartao;
	
	@CreationTimestamp
	private LocalDateTime dataCadastro;
	
	@NotBlank
	private String userAgent;
	
	@NotBlank
	private String enderecoIp;
	
	@Deprecated
	public AvisoViagem() {
	}

	public AvisoViagem(@NotBlank String destino, @NotNull @Future LocalDateTime dataViagem, @NotNull Cartao cartao, @NotBlank String userAgent,
			@NotBlank String enderecoIp) {
		this.destino = destino;
		this.dataViagem = dataViagem;
		this.cartao = cartao;
		this.userAgent = userAgent;
		this.enderecoIp = enderecoIp;
	}
}
