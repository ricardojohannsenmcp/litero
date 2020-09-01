package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.litero.camara.model.Area;
import br.com.litero.camara.repositorios.AreaRepository;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class AreaMB implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private AreaRepository areaRepository;
	
	@Inject
	private FacesMessages messages;
	
	private List<Area> areas;
	private Area area;
	private static final String EDICAO = "edicao";
	private static final String LISTAGEM = "lista";
	private String modo = LISTAGEM;


	
	public void listar() {
		areas = areaRepository.findAll();
	}


	public void novaArea() {
		this.area = new Area();
		modo = EDICAO;
	}

	public void editar(Long areaId) {
		this.area = areaRepository.findBy(areaId);
		modo = EDICAO;
	}

	public void cancelar() {
		this.area = null;
		modo = LISTAGEM;
	}

	public void remover(Area area) {
		try {
			areaRepository.remove(area);
			this.areas = areaRepository.findAll();
			messages.info("área removida com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			messages.error("área não pode ser removida");
		}
	}

	public void salvar() {
		
		
		areaRepository.save(area);
		
			messages.info("Dados salvos com sucesso");
			modo = LISTAGEM;
			this.area = null;
			this.areas = areaRepository.findAll();
		
	}

	public boolean isModoListagem() {
		return this.modo.equals(LISTAGEM);
	}

	public boolean isModoEdicao() {
		return this.modo.equals(EDICAO);
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public String getModo() {
		return modo;
	}


	
}
