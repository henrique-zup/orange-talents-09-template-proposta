package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovaInclusaoCarteiraRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovaInclusaoCarteiraResponse;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoBloqueioCartaoRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoBloqueioCartaoResponse;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoResponse;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoSistemaAvisoViagemRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoSistemaAvisoViagemResponse;

@FeignClient(name = "sistema-cartoes", url="${url.service.sistema-cartoes}")
public interface SistemaCartoesClient {
	
	@PostMapping("/cartoes")
	NovoCartaoResponse gerarCartao(@RequestBody NovoCartaoRequest request);
	
	@PostMapping("/cartoes/{numeroCartao}/bloqueios")
	NovoBloqueioCartaoResponse notificarBloqueio(@PathVariable String numeroCartao, @RequestBody NovoBloqueioCartaoRequest request);

	@PostMapping("/cartoes/{numeroCartao}/avisos")
	NovoSistemaAvisoViagemResponse notificarAvisoViagem(@PathVariable String numeroCartao,
			@RequestBody NovoSistemaAvisoViagemRequest novoSistemaAvisoViagemRequest);

	@PostMapping("/cartoes/{numeroCartao}/carteiras")
	NovaInclusaoCarteiraResponse associarCarteira(@PathVariable String numeroCartao, @RequestBody NovaInclusaoCarteiraRequest novaInclusaoCarteiraRequest);

}
