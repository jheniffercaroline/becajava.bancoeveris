package br.bancoEveris.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.bancoEveris.app.model.BaseResponse;
import br.bancoEveris.app.model.Conta;
import br.bancoEveris.app.request.ContaRequest;
import br.bancoEveris.app.response.ListContaResponse;
import br.bancoEveris.app.response.ContaResponse;
import br.bancoEveris.app.service.ContaService;

@RequestMapping
@RestController("/contas")
public class ContaController extends BaseController {

	private final ContaService _service;
	
	@Autowired
	public ContaController(ContaService service) {
		_service = service;

	}

	@PostMapping
	public ResponseEntity inserir() {

		try
		{
			BaseResponse base = _service.inserir();
			return ResponseEntity.status(base.statusCode).body(base);
		} catch (Exception e)
		{
			return ResponseEntity.status(errorBase.statusCode).body(errorBase);
		}

	}
	
	@GetMapping
	public ResponseEntity listar() {
		try
		{
			ListContaResponse contas = _service.listar();			
			return ResponseEntity.status(contas.statusCode).body(contas);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(errorBase.statusCode).body(errorBase);
		}
		
	}
	
	@GetMapping(path="/obter/{id}")
	public ResponseEntity obter(@PathVariable Long id) {
		try
		{
			ContaResponse response = _service.obter(id);
			return ResponseEntity.status(response.statusCode).body(response);
		}
		catch(Exception e)
		{
			return ResponseEntity.status(errorBase.statusCode).body(errorBase);
		}
	}
	
	@PutMapping(path="/editar/{id}")
	public ResponseEntity editar(@RequestBody ContaRequest contaRequest, @PathVariable Long id) {
		try
		{
			BaseResponse base = _service.editar(id, contaRequest);
			return ResponseEntity.status(base.statusCode).body(base);
		}
		catch(Exception e)
		{
			return ResponseEntity.status(errorBase.statusCode).body(errorBase);
		}
	}
	
	@DeleteMapping(path="/deletar/{id}")
	public ResponseEntity deletar(@PathVariable Long id) {
		try
		{
			BaseResponse base = _service.deletar(id);
			return ResponseEntity.status(base.statusCode).build();
		}
		catch(Exception e)
		{
			return ResponseEntity.status(errorBase.statusCode).body(errorBase);
		}
	}
	
	@GetMapping(path="/saldo/{hash}")
	public ResponseEntity Saldo(@PathVariable String hash) {
		try
		{
			BaseResponse response = _service.saldo(hash);
			return ResponseEntity.status(response.statusCode).body(response);
		}
		catch(Exception e)
		{
			return ResponseEntity.status(errorBase.statusCode).body(errorBase);
		}
	}
}

