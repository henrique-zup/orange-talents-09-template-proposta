package br.com.zupacademy.henriquecesar.propostas.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;

public interface CartaoRepository extends CrudRepository<Cartao, UUID>{

}
