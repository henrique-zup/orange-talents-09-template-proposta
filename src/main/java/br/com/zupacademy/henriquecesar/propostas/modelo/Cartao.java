package br.com.zupacademy.henriquecesar.propostas.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoResponse;
import br.com.zupacademy.henriquecesar.propostas.repository.CartaoRepository;

@Entity
public class Cartao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length = 16)
	private UUID id;
	
	@NotBlank
	private String titular;
	
	@NotBlank
	private String numeroCartao;
	
	@NotNull
	private LocalDateTime dataEmissao;
	
	@NotNull
	private BigDecimal limite;
	
	@NotNull 
	@Range(min = 1, max=31)
	private Integer diaVencimento;
	
	@OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
	private List<Biometria> biometrias;
	
	@OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
	private List<BloqueioCartao> bloqueios;

	@OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
	private List<AvisoViagem> avisosViagem;

	@Deprecated
	public Cartao() {
	}

	private Cartao(@NotBlank String titular, @NotBlank String numeroCartao, @NotNull LocalDateTime dataEmissao,
			@NotNull BigDecimal limite, @NotNull @Range(min = 1, max = 31) Integer diaVencimento) {
		this.titular = titular;
		this.numeroCartao = numeroCartao;
		this.dataEmissao = dataEmissao;
		this.limite = limite;
		this.diaVencimento = diaVencimento;
	}

	public static Cartao buildCartao(NovoCartaoResponse response) {
		return new Cartao(response.getTitular(),
				response.getId(), 
				response.getEmitidoEm(), 
				response.getLimite(),
				response.getVencimento().getDia());
	}
	
	public String getNumeroCartao(Exibicao exibicao) {
		if (exibicao.equals(Exibicao.OFUSCADO)) {
			return String.format("%1$sXX-XXXX-XXXX-%2$s", 
					numeroCartao.substring(0, 2), numeroCartao.substring(15));
		}
		return numeroCartao;
	}

	public UUID getId() {
		return id;
	}
	
	public Biometria adicionarBiometria(Biometria biometria, CartaoRepository repository) {
		this.biometrias.add(biometria);
		repository.save(this);
		return biometrias.get(biometrias.size() - 1);
	}

	public BloqueioCartao bloquear(BloqueioCartao bloqueioCartao, CartaoRepository repository) {
		this.bloqueios.add(bloqueioCartao);
		repository.save(this);
		return bloqueios.get(bloqueios.size() - 1);
	}

	public boolean isBloqueado(EntityManager manager) {
		return !manager
		.createQuery("SELECT b from BloqueioCartao b WHERE b.cartao = :cartao AND b.ativo = true")
		.setParameter("cartao", this)
		.getResultList().isEmpty();
	}

	public AvisoViagem adicionarAvisoViagem(AvisoViagem avisoViagem, CartaoRepository repository) {
		avisosViagem.add(avisoViagem);
		repository.save(this);
		return avisosViagem.get(avisosViagem.size() - 1);
	}
}
