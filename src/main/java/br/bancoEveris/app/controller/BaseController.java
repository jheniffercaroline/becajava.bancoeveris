package br.bancoEveris.app.controller;

import br.bancoEveris.app.model.BaseResponse;

public class BaseController {

	public BaseResponse errorBase = new BaseResponse();

	public BaseController() {
		errorBase.statusCode = 500;
		errorBase.message = "Ocorreu um erro inesperado";
	}

}
