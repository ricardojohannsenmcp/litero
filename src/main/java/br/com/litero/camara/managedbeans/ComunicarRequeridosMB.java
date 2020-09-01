package br.com.litero.camara.managedbeans;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.NumberTool;

import br.com.litero.camara.model.Convite;
import br.com.litero.camara.model.Parte;
import br.com.litero.camara.repositorios.ConviteRepository;
import br.com.litero.camara.service.MailSender;

@Named
@RequestScoped
public class ComunicarRequeridosMB {
	
	
	private List<Parte> requeridos;
	
	
	@Inject
	private MailSender mailSender;
	
	
	@Inject
	private ConviteRepository conviteRepository;
	
	//@Inject
	//private UsuarioRepository usuarioRepository;
	
	
	@Inject
	private CasoMB casoMB;
	
	
	
	public void enviarConvite() {
		
		
		for(Parte parte : requeridos) {
			
			if(parte.getPessoa().getEmail()!= null && !parte.getPessoa().getEmail().isEmpty()) {
				
				Convite convite = new Convite(parte.getCaso(), parte.getPessoa());
				conviteRepository.save(convite);
				InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/templates/emails/convite.template");
				VelocityContext ctx = new VelocityContext();
				Writer writer = new StringWriter();
				ctx.put("requerido",parte.getPessoa().getNome());
				ctx.put("caso",casoMB.getCaso());
				ctx.put("numberTool", new NumberTool());
				ctx.put("locale", new Locale("pt","br"));
				ctx.put("token", convite.getToken());
				 Reader templateReader = new BufferedReader(new InputStreamReader(is));
		        Velocity.evaluate(ctx, writer, "log tag name", templateReader);
				String msg = writer.toString();
				mailSender.enviar("contato@rjsisweb.com.br",parte.getPessoa().getEmail(), "cadastro de caso",msg);
			
			}
		}
		
	}

	public List<Parte> getRequeridos() {
		return requeridos;
	}

	public void setRequeridos(List<Parte> requeridos) {
		this.requeridos = requeridos;
	}
	
	
	
	
	
	
	
	
	
	
	

}
