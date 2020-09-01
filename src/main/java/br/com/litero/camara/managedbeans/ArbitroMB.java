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

import br.com.litero.camara.model.Arbitro;
import br.com.litero.camara.model.Area;
import br.com.litero.camara.model.Especialidade;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.Ramo;
import br.com.litero.camara.model.TipoUsuario;
import br.com.litero.camara.repositorios.AreaRepository;
import br.com.litero.camara.repositorios.EspecialidadeRepository;
import br.com.litero.camara.repositorios.RamoRepository;
import br.com.litero.camara.service.ArbitroService;
import br.com.litero.camara.service.PessoaService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class ArbitroMB implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private ArbitroService arbitroService;
	@Inject
	private PessoaService pessoaService;
	@Inject
	private FacesMessages messages;
	
	@Inject
	private EspecialidadeRepository especialidadeRepository;
	
	private List<SelectItem> categories;

	
	@Inject
	private RamoRepository ramoRepository;


	@Inject
	private AreaRepository areaRepository;
	
	
	private Especialidade especialidadeSelecionada=  null;
	
	private List<Especialidade> especialidades;
	
	private List<Arbitro> arbitros;
	private Arbitro arbitro;
	private static final String EDICAO = "edicao";
	private static final String LISTAGEM = "lista";
	private String modo = LISTAGEM;

	public void listar() {
		arbitros = arbitroService.recuperarTodos();
	}

	public List<Pessoa> buscarPartes(String filtro){
		return pessoaService.buscarPessoas(filtro);
	}

	public void novoArbitro() {
		this.arbitro = new Arbitro();
		modo = EDICAO;
	}

	public void editarArbitro(Long arbitroId) {
		this.arbitro = arbitroService.recuperar(arbitroId);
		modo = EDICAO;
	}

	public void cancelar() {
		this.arbitro = null;
		modo = LISTAGEM;
	}
	
	
	
	
	
	
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


	public void removerArbitro(Arbitro arbitro) {
		try {
			arbitroService.remover(arbitro);
			this.arbitros = arbitroService.recuperarTodos();
			messages.info("Árbitro removido com sucesso");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messages.error("Árbitro não pode ser removido");
		}
	}
	
	
	
	
	public void adicionarEspecialidade() {
		
		
		
		if(especialidadeSelecionada != null) {
		
		this.arbitro.getEspecialidades().add(especialidadeSelecionada);
		this.especialidadeSelecionada = null;
		}else {
			
			messages.error("Selecione uma especialidade");
		}
	}
	
	
	public void removerEspecialidade(Especialidade especialidade) {
		
		
		this.arbitro.getEspecialidades().remove(especialidade);
	}

	public void salvar() {
		
		if(arbitro.getPessoa().getUsuario()!=null) {
			if(arbitro.getPessoa().getUsuario().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
				messages.error("Administradores não podem ser árbitros");
			}
			else {
				arbitroService.adicionarOuAtualizar(arbitro);
				messages.info("Dados salvos com sucesso");
				modo = LISTAGEM;
				this.arbitro = null;
				this.arbitros = arbitroService.recuperarTodos();
			}
		}
		else {
			arbitroService.adicionarOuAtualizar(arbitro);
			messages.info("Dados salvos com sucesso");
			modo = LISTAGEM;
			this.arbitro = null;
			this.arbitros = arbitroService.recuperarTodos();
		}
	}

	public boolean isModoListagem() {
		return this.modo.equals(LISTAGEM);
	}

	public boolean isModoEdicao() {
		return this.modo.equals(EDICAO);
	}

	public List<Arbitro> getArbitros() {
		return arbitros;
	}

	public Arbitro getArbitro() {
		return arbitro;
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
