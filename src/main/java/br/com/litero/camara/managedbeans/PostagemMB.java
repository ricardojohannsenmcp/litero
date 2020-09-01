package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import br.com.litero.camara.exceptions.NegocioException;
import br.com.litero.camara.model.Anexo;
import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Postagem;
import br.com.litero.camara.persistence.Transactional;
import br.com.litero.camara.repositorios.CasoRepository;
import br.com.litero.camara.repositorios.PostagemRepository;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.util.arquivos.FileSaver;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class PostagemMB implements Serializable{


	private static final long serialVersionUID = 1L;


	private Postagem postagemAtual;
	private List<Postagem> postagemList;
	@Inject
	private CasoMB casoMB;
	private Caso casoAtual;
	@Inject
	private PostagemRepository postagemRepository;
	@Inject
	private CasoRepository casoRepository;
	@Inject
	private UsuarioWeb usuarioWeb;
	@Inject
	private FacesMessages messages;
	@Inject
	private FileSaver fileSaver;
	
	private Long postId;
	
	private Long casoId;


	@PreDestroy
	public void destroy() {

		postagemAtual = null;
		if(null != postagemList) {
			postagemList.clear();
			postagemList = null;
		}
		casoMB = null;
		casoAtual = null;
		postId = null;
	}




	public void init() {

		casoAtual = casoMB.getCaso();
		postagemList = postagemRepository.buscarTodosPorCaso(casoAtual.getCasoId());
	}



	public void load() {
		
		if(postId != null) {
			
			
		}else {
			
			if(casoId != null) {
				
				casoAtual = casoRepository.findBy(casoId);
				
				postagemAtual = new Postagem(casoAtual,usuarioWeb.getUsuario().getPessoa());

				
			}else {
				
				throw new NegocioException("Erro de requisição. Utilize um link do sistema");
			}
			
		}

	}



	@Transactional
	public void salvar() {
		
		

		postagemAtual = postagemRepository.save(postagemAtual);
		
		messages.info("nova postagem adicionada");
	}


	@Transactional	
	public void aprovar() {

		int index = postagemList.indexOf(postagemAtual);
		postagemAtual.aprovar(usuarioWeb.getUsuario().getPessoa());//verificar se é o mediador do caso
		postagemRepository.save(postagemAtual);
		postagemList.set(index, postagemAtual);
		RequestContext.getCurrentInstance().update("tab_propostas:posts");
		messages.info("postagem aprovada");
	}




	public void uploadAnexos(FileUploadEvent event) {

		String finalFileName =  fileSaver.write(event.getFile().getFileName(), event.getFile().getContents());
		Anexo anexo = new Anexo(postagemAtual,finalFileName);
		postagemAtual.getAnexos().add(anexo);
	}


	public void removerAnexo(Anexo anexo) {
		if(anexo != null) {
			if(postagemAtual.getAnexos().contains(anexo)) {
				postagemAtual.getAnexos().remove(anexo);
			}
		}
		anexo = null;	
	}



	public Postagem getPostagemAtual() {
		return postagemAtual;
	}

	public void setPostagemAtual(Postagem postagemAtual) {
		this.postagemAtual = postagemAtual;
	}

	public List<Postagem> getPostagemList() {
		return postagemList;
	}




	public Caso getCasoAtual() {
		return casoAtual;
	}




	public Long getPostId() {
		return postId;
	}




	public void setPostId(Long postId) {
		this.postId = postId;
	}




	public Long getCasoId() {
		return casoId;
	}




	public void setCasoId(Long casoId) {
		this.casoId = casoId;
	}



}
