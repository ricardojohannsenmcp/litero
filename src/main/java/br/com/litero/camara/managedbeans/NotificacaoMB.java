package br.com.litero.camara.managedbeans;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.com.litero.camara.model.Notificacao;
import br.com.litero.camara.persistence.Transactional;
import br.com.litero.camara.repositorios.NotificacaoRepository;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@RequestScoped
public class NotificacaoMB {
	
	@Inject
	private UsuarioWeb usuarioWeb;
	@Inject
	private NotificacaoRepository notificacaoRepository;
	@Inject
	private FacesMessages messages;
	
	
	@Transactional
	public void mostrarMensagens() {
		List<Notificacao> notificacoes = notificacaoRepository.buscarNotificacoesNaoLidas(usuarioWeb.getUsuario()
				.getPessoa().getPessoaId());
		notificacoes.forEach(n ->{
			messages.info(n.getTexto());
			n.abrir();
			notificacaoRepository.save(n);
		});
		
		usuarioWeb.add(0L);
		RequestContext.getCurrentInstance().update("notificacoes");
	}
	
	public void quantidadeDeMensagensNaoLidas() {
		long quantidade =  notificacaoRepository.quantidadeNotificacoesNaoLidas(usuarioWeb.getUsuario()
				.getPessoa().getPessoaId());
		
		
		usuarioWeb.add(quantidade);
	}


	
	
	

}
