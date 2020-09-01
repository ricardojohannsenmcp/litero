package br.com.litero.camara.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.deltaspike.core.api.provider.BeanProvider;

import br.com.litero.camara.model.Mediador;
import br.com.litero.camara.repositorios.MediadorRepository;

@FacesConverter(forClass = Mediador.class,value="mediadorConverter")
public class MediadorConverter implements Converter {

	private MediadorRepository mediadorRepository;

	public MediadorConverter() {
		this.mediadorRepository = BeanProvider.getContextualReference(MediadorRepository.class, false);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		Mediador retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = mediadorRepository.findBy(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value != null) {

			Mediador mediador = (Mediador) value;
			return mediador.getMediadorId() == null ? null : mediador.getMediadorId().toString();
		}

		return null;
	}

}
