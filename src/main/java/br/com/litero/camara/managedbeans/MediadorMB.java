package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.litero.camara.model.Area;
import br.com.litero.camara.model.Especialidade;
import br.com.litero.camara.model.Mediador;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.Ramo;
import br.com.litero.camara.model.TipoUsuario;
import br.com.litero.camara.repositorios.AreaRepository;
import br.com.litero.camara.repositorios.EspecialidadeRepository;
import br.com.litero.camara.repositorios.RamoRepository;
import br.com.litero.camara.service.MediadorService;
import br.com.litero.camara.service.PessoaService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class MediadorMB implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private MediadorService mediadorService;
	@Inject
	private PessoaService pessoaService;
	@Inject
	private FacesMessages messages;

	private List<Mediador> mediadores;
	private Mediador mediador;
	private static final String EDICAO = "edicao";
	private static final String LISTAGEM = "lista";
	private String modo = LISTAGEM;


	private List<SelectItem> categories;

	@Inject
	private EspecialidadeRepository especialidadeRepository;



	@Inject
	private RamoRepository ramoRepository;


	@Inject
	private AreaRepository areaRepository;
	
	
	private Especialidade especialidadeSelecionada=  null;




	@PostConstruct
	public void postConstruct() {

    categories = new ArrayList<>();

		List<Area> areas =  areaRepository.findAll();

		for(Area area : areas) {


			SelectItemGroup grupo =  new SelectItemGroup(area.getNome());

			List<Ramo> ramos =  ramoRepository.buscarPorArea(area.getAreaId());

			List<SelectItem> subgrupos  = new ArrayList<>();

			for(Ramo ramo : ramos) {

				SelectItemGroup subgrupo =  new SelectItemGroup(ramo.getNome());


				List<Especialidade> especialidades =  especialidadeRepository.buscarPorRamo(ramo.getRamoId());

				List<SelectItem> options =  new ArrayList<>();

				for(Especialidade especialidade : especialidades) {

					SelectItem item = new SelectItem(especialidade, especialidade.getNome());
					options.add(item);
				}

				subgrupo.setSelectItems(options.toArray(new SelectItem[especialidades.size()]));

				subgrupos.add(subgrupo);


			}

			grupo.setSelectItems(subgrupos.toArray(new SelectItem[ramos.size()]));
			categories.add(grupo);

		}


	}



	private List<Especialidade> especialidades;

	public void listar() {
		mediadores = mediadorService.recuperarTodos();
	}

	public List<Pessoa> buscarPartes(String filtro){
		return pessoaService.buscarPessoas(filtro);
	}

	public void novoMediador() {
		this.mediador = new Mediador();
		modo = EDICAO;
	}

	public void editarMediador(Long mediadorId) {
		this.mediador = mediadorService.recuperar(mediadorId);
		modo = EDICAO;
	}

	public void cancelar() {
		this.mediador = null;
		modo = LISTAGEM;
	}
	
	
	
	public void adicionarEspecialidade() {
		
		if(especialidadeSelecionada != null) {
		
		this.mediador.getEspecialidades().add(especialidadeSelecionada);
		this.especialidadeSelecionada = null;
		}else {
			
			messages.error("Selecione uma especialidade");
		}
	}
	
	
	public void removerEspecialidade(Especialidade especialidade) {
		
		
		this.mediador.getEspecialidades().remove(especialidade);
	}
	
	
	

	public void removerMediador(Mediador mediador) {
		try {
			mediadorService.remover(mediador);
			this.mediadores = mediadorService.recuperarTodos();
			messages.info("Mediador removido com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			messages.error("Mediador não pode ser removido");
		}
	}

	public void salvar() {

		if(mediador.getPessoa().getUsuario()!=null) {
			if(mediador.getPessoa().getUsuario().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
				messages.error("Administradores não podem ser mediadores");
			}
			else {
				mediadorService.adicionarOuAtualizar(mediador);
				messages.info("Dados salvos com sucesso");
				modo = LISTAGEM;
				this.mediador = null;
				this.mediadores = mediadorService.recuperarTodos();
			}
		}
		else {
			mediadorService.adicionarOuAtualizar(mediador);
			messages.info("Dados salvos com sucesso");
			modo = LISTAGEM;
			this.mediador = null;
			this.mediadores = mediadorService.recuperarTodos();
		}
	}

	public boolean isModoListagem() {
		return this.modo.equals(LISTAGEM);
	}

	public boolean isModoEdicao() {
		return this.modo.equals(EDICAO);
	}

	public List<Mediador> getMediadores() {
		return mediadores;
	}

	public Mediador getMediador() {
		return mediador;
	}

	public List<Especialidade> getEspecialidades(){

		if(especialidades == null) {

			especialidades = especialidadeRepository.findAll();
		}
		return especialidades;
	}

	public List<SelectItem> getCategories() {
		return categories;
	}

	public Especialidade getEspecialidadeSelecionada() {
		return especialidadeSelecionada;
	}

	public void setEspecialidadeSelecionada(Especialidade especialidadeSelecionada) {
		this.especialidadeSelecionada = especialidadeSelecionada;
	}
	
	
	
	

}
