package br.bancoEveris.app.model;

import javax.persistence.Transient;

public class BaseResponse {
	
	@Transient
	public int statusCode;
	
	@Transient
	public String message;
	
	

}
