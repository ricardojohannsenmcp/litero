package br.com.litero.camara.exceptions;

public class NegocioException extends RuntimeException{
	

	private static final long serialVersionUID = 1L;

	public NegocioException(String message) {
		
		 super(message);
	}
	
	public NegocioException(Throwable t) {
		
		 super(t);
	}
	

}
