package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.StatusCaso;
import br.com.litero.camara.repositorios.CasoRepository;
import br.com.litero.camara.security.UsuarioWeb;

@Named
@ViewScoped
public class ListaCasosMB implements Serializable {

	
	
	private static final long serialVersionUID = 1L;

	@Inject
	private CasoRepository casoDAO;
	
	@Inject
	private UsuarioWeb usuarioWeb;
	
	private final String GRID = "grid";
	
	private final String LIST = "list";
	
	private String modoExibicao = GRID;
	
	private String nomeParticipante;
	
	private String cpfCnpjOabParticipante;
	
	private Long casoId;
	
	private StatusCaso statusSelecionado;
	
	private Date dataCriacao;
	
	
	private List<Caso> casos;
	

	
	
	
	public void listar() {
		
		if(usuarioWeb.isAdministrador()) {
			//this.casos = casoService.buscarPorResponsavel(usuarioWeb.getUsuario().getLogin());
			this.casos = casoDAO.buscarCasoFiltroAdministrador(null, null, null, null, null);
		}
		else if(usuarioWeb.isMediador()) {
			this.casos = casoDAO.buscarCasoDoMediador(usuarioWeb.getUsuario().getPessoa().getPessoaId());
			//this.casos = casoDAO.buscarCasoFiltroMediador(casoId, statusSelecionado, dataCriacao, nomeParticipante, cpfCnpjOabParticipante, usuarioWeb.getUsuario().getLogin());
		}
		else {
			this.casos = casoDAO.buscarCasosDoUsuario(usuarioWeb.getUsuario().getUsuarioId());
			//this.casos = casoDAO.buscarCasoFiltroParte(casoId, statusSelecionado, dataCriacao, nomeParticipante, cpfCnpjOabParticipante, usuarioWeb.getUsuario().getLogin());
		}
		
	}
	
	
	public void pesquisar() {
		
		if(usuarioWeb.isAdministrador()) {
			this.casos = casoDAO.buscarCasoFiltroAdministrador(casoId, statusSelecionado, dataCriacao, nomeParticipante, cpfCnpjOabParticipante);
		}
		else if(usuarioWeb.isMediador()) {
			this.casos = casoDAO.buscarCasoFiltroMediador(casoId, statusSelecionado, dataCriacao, nomeParticipante, cpfCnpjOabParticipante, usuarioWeb.getUsuario().getLogin());
		}
		else {
			this.casos = casoDAO.buscarCasoFiltroParte(casoId, statusSelecionado, dataCriacao, nomeParticipante, cpfCnpjOabParticipante, usuarioWeb.getUsuario().getLogin());
		}
		
		
	}
	
	
	
	
	public List<Caso> getCasos() {
		return casos;
	}






	public String getModoExibicao() {
		return modoExibicao;
	}
	
	

	public void exibirComoGrid() {
		this.modoExibicao = GRID;
	}
	
	public void exibirComoList() {
		this.modoExibicao = LIST;
	}
	
	
	public boolean isGrid() {
		return this.modoExibicao.equals(GRID);
	}






	public String getNomeParticipante() {
		return nomeParticipante;
	}






	public void setNomeParticipante(String nomeParticipante) {
		this.nomeParticipante = nomeParticipante;
	}






	public String getCpfCnpjOabParticipante() {
		return cpfCnpjOabParticipante;
	}






	public void setCpfCnpjOabParticipante(String cpfCnpjOabParticipante) {
		this.cpfCnpjOabParticipante = cpfCnpjOabParticipante;
	}






	






	



	public Long getCasoId() {
		return casoId;
	}


	public void setCasoId(Long casoId) {
		this.casoId = casoId;
	}
	
	public StatusCaso[] getStatus() {
		return StatusCaso.values();
	}


	public Date getDataCriacao() {
		return dataCriacao;
	}


	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}


	public StatusCaso getStatusSelecionado() {
		return statusSelecionado;
	}


	public void setStatusSelecionado(StatusCaso statusSelecionado) {
		this.statusSelecionado = statusSelecionado;
	}
	
	
	

}
