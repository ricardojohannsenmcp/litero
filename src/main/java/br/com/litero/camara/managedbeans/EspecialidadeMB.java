package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.litero.camara.model.Area;
import br.com.litero.camara.model.Especialidade;
import br.com.litero.camara.model.Ramo;
import br.com.litero.camara.repositorios.AreaRepository;
import br.com.litero.camara.repositorios.EspecialidadeRepository;
import br.com.litero.camara.repositorios.RamoRepository;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class EspecialidadeMB implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EspecialidadeRepository especialidadeRepository;
	
	@Inject
	private FacesMessages messages;
	
	@Inject
	private RamoRepository ramoRepository;
	
	@Inject
	private AreaRepository areaRepository;
	
	private List<Ramo> ramos;
	
	private List<Area> areas;
	
	
	private Area areaSelecionada;
	
	
	
	
	private List<Especialidade> especialidades;
	private Especialidade especialidade;
	private static final String EDICAO = "edicao";
	private static final String LISTAGEM = "lista";
	private String modo = LISTAGEM;


	
	public void listar() {
		especialidades = especialidadeRepository.findAll();
	}

	public void novo() {
		this.especialidade = new Especialidade();
		this.areaSelecionada = null;
		modo = EDICAO;
	}

	public void editar(Long especialidadeId) {
		this.especialidade = especialidadeRepository.findBy(especialidadeId);
		
		this.areaSelecionada =  especialidade.getRamo().getArea();
		this.ramos = ramoRepository.buscarPorArea(areaSelecionada.getAreaId());

		
		modo = EDICAO;
	}

	public void cancelar() {
		this.especialidade = null;
		this.areaSelecionada =  null;
		modo = LISTAGEM;
	}

	public void remover(Especialidade especialidade) {
		try {
			especialidadeRepository.remove(especialidade);
			this.especialidades = especialidadeRepository.findAll();
			messages.info("Especialidade removida com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			messages.error("especialidade não pode ser removida");
		}
	}

	public void salvar() {
		
		
		especialidadeRepository.save(especialidade);
		
			messages.info("Dados salvos com sucesso");
			modo = LISTAGEM;
			this.especialidade = null;
			this.especialidades = especialidadeRepository.findAll();
		
	}

	public boolean isModoListagem() {
		return this.modo.equals(LISTAGEM);
	}

	public boolean isModoEdicao() {
		return this.modo.equals(EDICAO);
	}
	
	
	public void selecionarArea() {
		
		
		
		ramos =  ramoRepository.buscarPorArea(areaSelecionada.getAreaId());
		
		especialidade.setRamo(null);
	}

	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}

	public List<Ramo> getRamos() {
		
		
		
		return ramos;
	}

	public List<Especialidade> getEspecialidades() {
		return especialidades;
	}

	public String getModo() {
		return modo;
	}

	public Area getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(Area areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}

	public List<Area> getAreas() {
		
		if(areas == null) {
			
			areas = areaRepository.findAll();
		}
		return areas;
	}
	
	
	

	

	
}
