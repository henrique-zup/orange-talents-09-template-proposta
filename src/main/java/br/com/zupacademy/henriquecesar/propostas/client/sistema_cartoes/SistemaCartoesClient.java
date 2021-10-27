package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoResponse;

@FeignClient(name = "sistema-cartoes", url="${url.service.sistema-cartoes}")
public interface SistemaCartoesClient {
	
	@PostMapping("/cartoes")
	NovoCartaoResponse gerarCartao(@RequestBody NovoCartaoRequest request);

}