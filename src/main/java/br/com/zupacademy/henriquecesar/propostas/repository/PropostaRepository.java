package br.com.zupacademy.henriquecesar.propostas.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.zupacademy.henriquecesar.propostas.modelo.Proposta;

public interface PropostaRepository extends CrudRepository<Proposta, Long>{

}