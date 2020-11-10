package br.bancoEveris.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.bancoEveris.app.model.Conta;

@Repository
public interface  ContaRepository extends JpaRepository<Conta, Long>{
		
		Conta findByHash(String hash);
}
