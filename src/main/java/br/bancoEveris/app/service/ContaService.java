package br.bancoEveris.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.bancoEveris.app.model.BaseResponse;
import br.bancoEveris.app.model.Conta;
import br.bancoEveris.app.repository.ContaRepository;
import br.bancoEveris.app.request.ContaRequest;
import br.bancoEveris.app.response.ListContaResponse;
import br.bancoEveris.app.response.ContaResponse;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.*;

@Service
public class ContaService {

	final ContaRepository _repository;
	final OperacaoService _operacaoService;

	@Autowired
	public ContaService(ContaRepository repository, OperacaoService operacaoService) {
		_repository = repository;
		_operacaoService = operacaoService;

	}

	public BaseResponse inserir() {
		Conta conta = new Conta();
		BaseResponse base = new BaseResponse();
		String randomHash = "";

		SecureRandom random = new SecureRandom();
        randomHash = new BigInteger(130, random).toString(32);
		
		boolean existe = true;
		while (existe) {
			

			Conta checkConta = _repository.findByHash(randomHash);

			if (checkConta == null)
				existe = false;
		}

		conta.setHash(randomHash);

		_repository.save(conta);
		base.statusCode = 201;
		base.message = "Conta cadastrada com sucesso";
		return base;
	}

	public ListContaResponse listar() {
		List<Conta> contas = _repository.findAll();

		ListContaResponse base = new ListContaResponse();
		base.setContas(contas);
		base.statusCode = 200;
		base.message = "Contas obtidas com sucesso.";
		return base;
	}

	public ContaResponse obter(Long id) {
		Optional<Conta> conta = _repository.findById(id);
		ContaResponse response = new ContaResponse();

		if (conta == null) {

			response.statusCode = 400;
			response.message = "Id não encontrado.";
			return response;
		}

		response.setId(conta.get().getId());
		response.setHash(conta.get().getHash());
		response.setSaldo(conta.get().getSaldo());

		response.statusCode = 200;
		response.message = "Conta encontrada.";
		return response;
	}

	public BaseResponse editar(Long id, ContaRequest contaRequest) {
		Optional<Conta> conta = _repository.findById(id);
		BaseResponse base = new BaseResponse();
		Conta request = new Conta();

		if (conta.get().getId() == 0) {
			base.statusCode = 400;
			base.message = "Id não encontrado.";
			return base;
		}

		if (contaRequest.getHash() == "") {
			base.statusCode = 400;
			base.message = "Hash não informada.";
			return base;
		}

		Conta checkConta = _repository.findByHash(contaRequest.getHash());

		if (checkConta != null) {
			base.statusCode = 400;
			base.message = "Hash indisponível.";
			return base;
		}

		request.setId(id);
		request.setHash(contaRequest.getHash());

		_repository.save(request);
		base.statusCode = 200;
		base.message = "Conta atualizada.";
		return base;
	}

	public BaseResponse deletar(Long id) {
		Optional<Conta> conta = _repository.findById(id);

		BaseResponse base = new BaseResponse();

		if (conta.get().getId() == 0) {

			base.statusCode = 400;
			base.message = "Id não encontrado.";
			return base;

		}

		_repository.deleteById(id);
		base.statusCode = 200;
		base.message = "Conta deletada.";
		return base;
	}

	public ContaResponse saldo(String hash) {
		ContaResponse response = new ContaResponse();

		response.statusCode = 400;

		Conta lista = _repository.findByHash(hash);
		if (lista == null) {
			response.message = "Conta não encontrada.";
			return response;
		}

		double saldo = _operacaoService.saldo(lista.getId());
		response.setSaldo(saldo);
		response.setHash(lista.getHash());
		response.statusCode = 200;
		return response;
	}
}
