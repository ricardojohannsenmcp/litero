package br.com.litero.camara.exceptions;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class ApplicationExceptionHandlerFactory extends ExceptionHandlerFactory{
	
	
	private ExceptionHandlerFactory parent;
	
	
	
	public ApplicationExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
		}

	@Override
	public ExceptionHandler getExceptionHandler() {

		return new ApplicationExceptionHandler(parent.getExceptionHandler());
	}

}
