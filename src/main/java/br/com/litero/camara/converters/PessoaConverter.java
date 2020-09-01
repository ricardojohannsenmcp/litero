package br.com.litero.camara.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.deltaspike.core.api.provider.BeanProvider;

import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.repositorios.PessoaRepository;

@FacesConverter(forClass = Pessoa.class)
public class PessoaConverter implements Converter {
	
	
	private PessoaRepository pessoaRepository;
	
	
	
	
	
	public PessoaConverter() {
		 this.pessoaRepository = BeanProvider.getContextualReference(PessoaRepository.class, false);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Pessoa retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = pessoaRepository.findBy(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Pessoa parte = (Pessoa) value;
			return parte.getPessoaId() == null ? null : parte.getPessoaId().toString();
		}
		
		return "";
	}


}
