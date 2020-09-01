package br.com.litero.camara.util.jsf;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

@RequestScoped
public class FacesMessages {
	
	
	
	private void add(String message, Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		
		
		FacesMessage msg = new FacesMessage(message);
		msg.setSeverity(severity);
		
		context.addMessage(null, msg);
	}
	
	public void info(String message) {
		add(message, FacesMessage.SEVERITY_INFO);
	}
	
	public void error(String message) {
		add(message, FacesMessage.SEVERITY_ERROR);
	}

}
