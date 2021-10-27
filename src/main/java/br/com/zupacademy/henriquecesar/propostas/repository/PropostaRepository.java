package br.com.zupacademy.henriquecesar.propostas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import br.com.zupacademy.henriquecesar.propostas.modelo.Proposta;
import br.com.zupacademy.henriquecesar.propostas.modelo.PropostaStatus;

public interface PropostaRepository extends CrudRepository<Proposta, Long>{

    Optional<Proposta> findByDocumento(String documento);

	List<Proposta> findByCartaoIsNullAndStatusEquals(PropostaStatus status, Pageable pageRequest);

}
