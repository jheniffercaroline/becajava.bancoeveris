package br.bancoEveris.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.bancoEveris.app.model.BaseResponse;
import br.bancoEveris.app.model.Conta;
import br.bancoEveris.app.model.Operacao;
import br.bancoEveris.app.request.DepositoRequest;
import br.bancoEveris.app.request.SaqueRequest;
import br.bancoEveris.app.request.TransferenciaRequest;
import br.bancoEveris.app.service.OperacaoService;

@RestController
@RequestMapping("/operacoes")
public class OperacaoController extends BaseController {
	private Operacao operacao = new Operacao();
	private OperacaoService _service;

	public OperacaoController(OperacaoService service) {
		_service = service;
	}

	@PostMapping(path = "/deposito")
	public ResponseEntity deposito(@RequestBody DepositoRequest request) {
		try {

			operacao.setTipo("D");
			operacao.setValor(request.getValor());
			if (request.getHash() != "") {
				Conta conta = new Conta();
				conta.setHash(request.getHash());
				operacao.setContaDestino(conta);
			}

			BaseResponse base = _service.inserir(operacao);
			return ResponseEntity.status(base.statusCode).body(base);

		} catch (Exception e) {
			return ResponseEntity.status(errorBase.statusCode).body(errorBase);
		}
	}

	@PostMapping(path = "/saque")
	public ResponseEntity saque(@RequestBody SaqueRequest request) {
		try {

			operacao.setTipo("S");
			operacao.setValor(request.getValor());
			if (request.getHash() != "") {
				Conta conta = new Conta();
				conta.setHash(request.getHash());
				operacao.setContaOrigem(conta);
			}

			BaseResponse base = _service.inserir(operacao);
			return ResponseEntity.status(base.statusCode).body(base);

		} catch (Exception e) {
			return ResponseEntity.status(errorBase.statusCode).body(errorBase);
		}
	}

	@PostMapping(path = "/transferencia")
	public ResponseEntity transferencia(@RequestBody TransferenciaRequest request) {
		try {

			operacao.setTipo("T");
			operacao.setValor(request.getValor());
			if (request.getHashOrigem() != "") {
				Conta conta = new Conta();
				conta.setHash(request.getHashOrigem());
				operacao.setContaOrigem(conta);
			}
			if (request.getHashDestino() != "") {
				Conta conta = new Conta();
				conta.setHash(request.getHashDestino());
				operacao.setContaDestino(conta);
			}

			BaseResponse base = _service.inserir(operacao);
			return ResponseEntity.status(base.statusCode).body(base);

		} catch (Exception e) {
			return ResponseEntity.status(errorBase.statusCode).body(errorBase);
		}
	}
}
