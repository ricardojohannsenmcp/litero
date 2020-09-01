package br.com.litero.camara.exceptions;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;


public class ApplicationExceptionHandler extends ExceptionHandlerWrapper{
	
	
	private ExceptionHandler wrapped;
	
	
	public  ApplicationExceptionHandler( ExceptionHandler wrapped) {
		this.wrapped = wrapped;
		
		
	}
	
	
	
	public void handle() throws FacesException{
		
		Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();
		while(events.hasNext()) {
			
			ExceptionQueuedEvent event = events.next();	
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			Throwable exception =  context.getException();	
			NegocioException negocioException = getNegocioException(exception);
			boolean handled =  false;
			try {
			if(exception instanceof ViewExpiredException) {
				
				handled =  true;
				redirect("/");
				
			}else if(negocioException != null){
				handled = true;
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage msg = new FacesMessage(negocioException.getMessage());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				
				fc.addMessage(null, msg);
				
				
				
			}else {
				
				exception.printStackTrace();
				
				//remover comentario quando em produção
				//handled =  true;
				//redirect("/Erro.xhtml");
			}
			}finally {
				if(handled) {
				events.remove();
				}
			}
		}
		getWrapped().handle();
	}
	
	

	private NegocioException getNegocioException(Throwable exception) {
		
		if(exception instanceof NegocioException) {	
			return (NegocioException) exception;
		}else if(exception.getCause() != null ){
			getNegocioException(exception.getCause());
		}
		return null;
	}
	
	
	


	private void redirect(String page) {

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext externalContext = fc.getExternalContext();
			String contextPath = externalContext.getApplicationContextPath();
			externalContext.redirect(contextPath +page);
			fc.responseComplete();
		} catch (IOException e) {
			
			throw new FacesException(e);
		}
	}



	@Override
	public ExceptionHandler getWrapped() {

		return this.wrapped;
	}
	
	

}
