package br.com.litero.camara.util.validacao;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.litero.camara.model.TipoPessoa;

@FacesValidator("cnpjCpfValidator")
public class CnpjCpfValidator implements Validator{

	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {

		String cpfCnpj = (String) value;
		
	     UIInput confirmComponent = (UIInput) component;
	     
	     String tipo = (String) confirmComponent.getAttributes().get("tipo");
	    //  String tipo = (String) confirmComponent.getSubmittedValue();
	    
	      
	      
	      boolean valido = false;
	      
	      
	      if(cpfCnpj == null) {
	    	  
	    	  return;
	      }
	      
	  
	   
	   if(tipo.equals(TipoPessoa.FISICA.getDescricao())) {
		   
		   valido = CnpjCpf.isValidCPF(cpfCnpj);
	   }else {
		   
		   valido = CnpjCpf.isValidCNPJ(cpfCnpj);
	   }
	      
	   if(!valido) {
		   
	            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,null,"Formato Inválido"));
	        }
	   }
	      
		
		
	

}
