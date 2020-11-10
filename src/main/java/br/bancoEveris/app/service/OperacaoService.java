package br.bancoEveris.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.bancoEveris.app.model.BaseResponse;
import br.bancoEveris.app.model.Conta;
import br.bancoEveris.app.model.Operacao;
import br.bancoEveris.app.repository.ContaRepository;
import br.bancoEveris.app.repository.OperacaoRepository;

@Service
public class OperacaoService {

	final OperacaoRepository _repository;
	final ContaRepository _contaRepository;

	@Autowired
	public OperacaoService(OperacaoRepository repository, ContaRepository contaRepository) {
		_repository = repository;
		_contaRepository = contaRepository;
	}

	public double saldo(Long id) {
		double saldo = 0;

		Conta contaOrigem = new Conta();
		contaOrigem.setId(id);

		Conta contaDestino = new Conta();
		contaDestino.setId(id);

		List<Operacao> listaOrigem = _repository.findByContaOrigem(contaOrigem);
		List<Operacao> listaDestino = _repository.findByContaDestino(contaDestino);

		for (Operacao o : listaOrigem) {
			switch (o.getTipo()) {
			case "D":
				saldo += o.getValor();
				break;
			case "S":
				saldo -= o.getValor();
				break;
			case "T":
				saldo -= o.getValor();
				break;
			default:
				break;
			}
		}

		for (Operacao o : listaDestino) {
			switch (o.getTipo()) {
			case "D":
				saldo += o.getValor();
				break;
			case "S":
				saldo -= o.getValor();
				break;
			case "T":
				saldo += o.getValor();
				break;
			default:
				break;
			}
		}
		return saldo;
	}

	public BaseResponse inserir(Operacao operacao) {
		BaseResponse base = new BaseResponse();

		switch (operacao.getTipo()) {
		case "D":
			if (operacao.getContaDestino() == null) {
				base.statusCode = 400;
				base.message = "Conta para depósito não informada";
				return base;
			}
			if (operacao.getValor() == 0) {
				base.statusCode = 400;
				base.message = "Valor não pode ser zero";
				return base;
			}

			Conta checkConta = _contaRepository.findByHash(operacao.getContaDestino().getHash());
			if (checkConta == null ) {
				base.statusCode = 400;
				base.message = "Conta para depósito não localizada";
				return base;
			}
			operacao.setContaDestino(checkConta);


			break;

		case "S":
			if (operacao.getContaOrigem() == null) {
				base.statusCode = 400;
				base.message = "Conta para saque não localizada";
				return base;
			}
			if (operacao.getValor() == 0) {
				base.statusCode = 400;
				base.message = "Valor não pode ser zero";
				return base;
			}
			
			checkConta = _contaRepository.findByHash(operacao.getContaOrigem().getHash());
			if (checkConta == null ) {
				base.statusCode = 400;
				base.message = "Conta para saque não localizada";
				return base;
			}
			operacao.setContaOrigem(checkConta);
			break;

			
		case "T":
			if (operacao.getContaOrigem().getHash() == null || operacao.getContaDestino().getHash() == null) {
				base.statusCode = 400;
				base.message = "Uma das contas não foi informada";
				return base;
			}
			if (operacao.getValor() == 0) {
				base.statusCode = 400;
				base.message = "Valor não pode ser zero";
				return base;
			}
			
			Conta checkContaOrigem = _contaRepository.findByHash(operacao.getContaOrigem().getHash());
			Conta checkContaDestino = _contaRepository.findByHash(operacao.getContaDestino().getHash());
			
			if(checkContaOrigem == null || checkContaDestino == null) {
				base.statusCode = 400;
				base.message = "Conta para transferência não localizada.";
				return base;
			}
			
			operacao.setContaOrigem(checkContaOrigem);
			operacao.setContaDestino(checkContaDestino);
			break;

		}
		_repository.save(operacao);
		base.statusCode = 201;
		base.message = "Operação criada com sucesso.";

		return base;
	}

} 