package br.com.litero.camara.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.deltaspike.core.api.provider.BeanProvider;

import br.com.litero.camara.model.Arbitro;
import br.com.litero.camara.repositorios.ArbitroRepository;

@FacesConverter(forClass = Arbitro.class,value="arbitroConverter")
public class ArbitroConverter implements Converter {
	
	
	private ArbitroRepository arbitroRepository;
	
	
	
	
	
	public ArbitroConverter() {
		 this.arbitroRepository = BeanProvider.getContextualReference(ArbitroRepository.class, false);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		
		Arbitro retorno = null;
		
		
		
		if (value != null) {
			Long id = new Long(value);
			retorno = arbitroRepository.findBy(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		System.out.println("get as string");

		if (value != null) {
			System.out.println("value: "+value);
			
			Arbitro arbitro = (Arbitro) value;
			return arbitro.getArbitroId() == null ? null : arbitro.getArbitroId().toString();
		}
		
		return null;
	}


}
