package br.com.zupacademy.henriquecesar.propostas.modelo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zupacademy.henriquecesar.propostas.common.annotation.Base64;
import br.com.zupacademy.henriquecesar.propostas.dto.request.NovaBiometriaRequest;

@Entity
public class Biometria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Cartao cartao;

	@NotBlank
	@Base64
	@Column(columnDefinition = "TEXT")
	private String fingerPrint;

	@NotNull
	private LocalDateTime dataCadastro = LocalDateTime.now();

	@Deprecated
	public Biometria() {
	}

	private Biometria(@Valid Cartao cartao, @NotBlank @Base64 String fingerPrint) {
		this.cartao = cartao;
		this.fingerPrint = fingerPrint;
	}

	public static Biometria buildBiometria(@Valid Cartao cartao, @Valid NovaBiometriaRequest request) {
		return new Biometria(cartao, request.getFingerPrint());
	}

	public Long getId() {
		return id;
	}

}
