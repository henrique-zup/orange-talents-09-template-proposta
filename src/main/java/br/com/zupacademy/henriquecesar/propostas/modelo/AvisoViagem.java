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
	private LocalDateTime dataTerminoViagem;
	
	@ManyToOne(optional = false)
	private Cartao cartao;
	
	@CreationTimestamp
	private LocalDateTime dataCadastro;
	
	@NotBlank
	private String userAgent;
	
	@NotBlank
	private String enderecoIp;
	
	@NotNull
	private boolean sincronizado;
	
	@Deprecated
	public AvisoViagem() {
	}

	public AvisoViagem(@NotBlank String destino, @NotNull @Future LocalDateTime dataTerminoViagem, @NotNull Cartao cartao, @NotBlank String userAgent,
			@NotBlank String enderecoIp) {
		this.destino = destino;
		this.dataTerminoViagem = dataTerminoViagem;
		this.cartao = cartao;
		this.userAgent = userAgent;
		this.enderecoIp = enderecoIp;
	}

	public Cartao getCartao() {
		return cartao;
	}
	
	public String getDestino() {
		return destino;
	}

	public LocalDateTime getDataTerminoViagem() {
		return dataTerminoViagem;
	}
	
	public boolean isSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(boolean sincronizado) {
		this.sincronizado = sincronizado;
	}
}
