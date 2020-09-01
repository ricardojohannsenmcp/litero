package br.com.litero.camara.managedbeans;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.MotivoFinalizacao;
import br.com.litero.camara.model.StatusCaso;
import br.com.litero.camara.persistence.Transactional;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.CasoService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class CasoMB implements Serializable{

	private static final long serialVersionUID = -1516884415124666998L;

	@Inject
	private UsuarioWeb usuarioWeb;
	@Inject
	private FacesMessages messages;
	

	private Long casoId;
	private Caso caso;
	private StatusCaso statusSelecionado;
	
	@Inject
	private CasoService casoService;
	






	public void exibir() {
		this.caso =  casoService.recuperar(casoId);
		if(!caso.isAcessivelPor(usuarioWeb.getUsuario())) {
			throw new RuntimeException("Acesso Negado ao caso");
		}
		//TODO mudar para requerente apenas
      if(caso.getStatus().equals(StatusCaso.PROCESSO_COMUNICACAO) && usuarioWeb.getUsuario().getPessoa().isRequerentePrincipalCaso(caso)) {
			
			messages.info("Aguarde as partes contrárias aceitarem o convite para participar do caso");
		}
      
      if(caso.getStatus().equals(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR) && usuarioWeb.getUsuario().getPessoa().isRequerentePrincipalCaso(caso)) {
    	  
    	  
			messages.info("Para prosseguir o caso o próximo passo é a definição do mediador");

      }
      

	}



	public String aceitarCaso() {

		try {
			casoService.aceitar(caso,usuarioWeb.getUsuario());
		
			FacesContext.getCurrentInstance() .getExternalContext().getFlash().setKeepMessages(true);
			messages.info("Caso aceito com sucesso");
		} catch (Exception e) {
			messages.error(e.getMessage());
			return null;
		}
		return "Caso?faces-redirect=true&casoId="+caso.getCasoId();
	}


	public void prepararNegarCaso() {
		RequestContext.getCurrentInstance().execute("PF('dialogNegarCaso').show();");
	}


	@Transactional
	public String negarCaso() {
		
		
		casoService.negar(caso);
		
		FacesContext.getCurrentInstance() .getExternalContext().getFlash().setKeepMessages(true);
		messages.info("Caso negado com sucesso");
		return "Caso?faces-redirect=true&casoId="+caso.getCasoId();
	}
	
	
	
	public String processoDeComunicacao() {
		
		try {
			casoService.processoDeComunicacao(caso);
			
			FacesContext.getCurrentInstance() .getExternalContext().getFlash().setKeepMessages(true);
			messages.info("Caso em processo de comunicação");
			return "Caso?faces-redirect=true&casoId="+caso.getCasoId();
		} catch (Exception e) {
			messages.error(e.getMessage());
			return null;
		}
	}
	
	
	public void prepararComunicarRequeridos() {
		RequestContext.getCurrentInstance().execute("PF('dlgCom').show();");
	}



	public void prepararFinalizarCaso() {
		RequestContext.getCurrentInstance().execute("PF('dialogFinalizarCaso').show();");
	}
	
	
	



	@Transactional
	public String finalizarCaso() {
		
		if(caso.getMotivoFinalizacaoAux()==null) {
			messages.error("Escolha o motivo da finalização.");
			return null;
		}
		else {
			casoService.finalizar(caso,caso.getMotivoFinalizacaoAux(),usuarioWeb.getUsuario());
			
			FacesContext.getCurrentInstance() .getExternalContext().getFlash().setKeepMessages(true);
			messages.info("Caso finalizado com sucesso");
			return "Caso?faces-redirect=true&casoId="+caso.getCasoId();
		}
		//return "Caso?faces-redirect=true&casoId="+caso.getCasoId();
	}

	public String recarregarCaso() {

		return "Caso?faces-redirect=true&casoId="+caso.getCasoId();
	}

	
	public boolean isRequerentePrincipal() {
		return usuarioWeb.getUsuario().getPessoa().isRequerentePrincipalCaso(caso);
	}
	
	public boolean isRequeridoCaso() {
		return usuarioWeb.getUsuario().getPessoa().isRequerido(caso);
	}


	public Long getCasoId() {
		return casoId;
	}
	public void setCasoId(Long casoId) {
		this.casoId = casoId;
	}
	public Caso getCaso() {
		return caso;
	}



	public UsuarioWeb getUsuarioWeb() {
		return usuarioWeb;
	}

	public StatusCaso[] getStatus() {
		return StatusCaso.values();
	}

	public MotivoFinalizacao[] getMotivosFinalizacao() {
		return MotivoFinalizacao.values();
	}

	public StatusCaso getStatusSelecionado() {
		return statusSelecionado;
	}

	public void setStatusSelecionado(StatusCaso statusSelecionado) {
		this.statusSelecionado = statusSelecionado;
	}



}
