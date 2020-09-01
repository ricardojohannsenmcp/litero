package br.com.litero.camara.managedbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import br.com.litero.camara.chat.ChatEndPoint;
import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Mensagem;
import br.com.litero.camara.persistence.Transactional;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.MensagemService;
import br.com.litero.camara.util.arquivos.FileSaver;
import br.com.litero.camara.util.jsf.FacesMessages;
import br.com.litero.camara.util.json.JsonObjectMessage;

@Named
@ViewScoped
public class ChatMB implements Serializable{


	private static final long serialVersionUID = 1L;


	@Inject
	private FacesMessages messages;
	@Inject
	private UsuarioWeb usuarioWeb;
	@Inject
	private ChatEndPoint chatEndpoint;
	@Inject
	private MensagemService mensagemService;
	@Inject
	private FileSaver fileSaver;
	@Inject
	private CasoMB casoMB;
	@Inject
	private ChatRoomMB chatRoomMB;
	private Caso casoAtual;
	private List<Mensagem> mensagemList;

	private String textMessage;
	

	private Part arquivo;

	@PostConstruct
	public void construct() {

		init();
	}

	@PreDestroy
	public void destroy() {

		casoMB = null;
		casoAtual = null;
		textMessage = null;
		arquivo = null;
		if(null != mensagemList) {
			mensagemList.clear();
			mensagemList =  null;
		}
	}


	public void init() {

		casoAtual = casoMB.getCaso();
		mensagemList = mensagemService.recuperarMensagensPorCaso(casoAtual.getCasoId());
		chatRoomMB.off(casoAtual.getCasoId());
	}





	public void enviar() {

		if(textMessage != null && !textMessage.isEmpty()) {
			Mensagem mensagem = new Mensagem(casoAtual, usuarioWeb.getUsuario().getPessoa(), textMessage);
			salvar(mensagem);
			JsonObjectMessage jsonMessage =  new JsonObjectMessage(String.valueOf(mensagem.getCaso().getCasoId()), mensagem.toJson());
			chatEndpoint.send(jsonMessage);
			textMessage =  null;
			mensagem = null;
		}else {
			messages.error("não é permitido enviar uma mensagem vazia");
		}
	}


	public void enviarDocumento() {

		String finalFileName =  fileSaver.write(arquivo);
		Mensagem mensagem = new Mensagem(casoAtual,usuarioWeb.getUsuario().getPessoa(),arquivo.getSubmittedFileName(),finalFileName);
		salvar(mensagem);
		JsonObjectMessage jsonMessage =  new JsonObjectMessage(String.valueOf(mensagem.getCaso().getCasoId()),mensagem.toJson());
		chatEndpoint.send(jsonMessage);
		textMessage =  null;
		arquivo = null;
	}


	@Transactional
	private void salvar(Mensagem mensagem) {
		mensagemService.adiciona(mensagem);
	}
	
	
	
	
    public void ligaDesligaChat() {
    	
    	if(!chatRoomMB.isOnline(casoAtual.getCasoId())) {
    		chatRoomMB.on(casoAtual.getCasoId());
    	}else {
    		chatRoomMB.off(casoAtual.getCasoId());
    	}
    }
    
    
    public boolean isOnline() {
    	
    	return chatRoomMB.isOnline(casoAtual.getCasoId());
    }
	


	public void iniciarConferencia() {
		try {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect("https://meet.jit.si/"+casoAtual.getCasoId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public String getTextMessage() {
		return textMessage;
	}


	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}


	public Part getArquivo() {
		return arquivo;
	}


	public void setArquivo(Part arquivo) {
		this.arquivo = arquivo;
	}


	public List<Mensagem> getMensagemList() {
		return mensagemList;
	}



}
