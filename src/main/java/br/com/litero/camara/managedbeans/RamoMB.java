package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import br.com.litero.camara.model.Area;
import br.com.litero.camara.model.Ramo;
import br.com.litero.camara.repositorios.AreaRepository;
import br.com.litero.camara.repositorios.RamoRepository;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class RamoMB implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private RamoRepository ramoRepository;
	
	@Inject
	private FacesMessages messages;
	
	@Inject
	private AreaRepository areaRepository;
	
	private List<Area> areas;
	
	
	
	
	private List<Ramo> ramos;
	private Ramo ramo;
	private static final String EDICAO = "edicao";
	private static final String LISTAGEM = "lista";
	private String modo = LISTAGEM;


	
	public void listar() {
		ramos = ramoRepository.findAll();
	}
	
	public void novo() {
		this.ramo = new Ramo();
		modo = EDICAO;
	}

	public void editar(Long ramoId) {
		this.ramo = ramoRepository.findBy(ramoId);
		modo = EDICAO;
	}

	public void cancelar() {
		this.ramo = null;
		modo = LISTAGEM;
	}

	@Transactional
	public void remover(Ramo ramo) {
		try {
			ramoRepository.remove(ramo);
			this.ramos = ramoRepository.findAll();
			messages.info("Ramo removido com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			messages.error("ramo não pode ser removido");
		}
	}

	
	@Transactional
	public void salvar() {
		
		
		ramoRepository.save(ramo);
		
			messages.info("Dados salvos com sucesso");
			modo = LISTAGEM;
			this.ramo = null;
			this.ramos = ramoRepository.findAll();
		
	}

	public boolean isModoListagem() {
		return this.modo.equals(LISTAGEM);
	}

	public boolean isModoEdicao() {
		return this.modo.equals(EDICAO);
	}

	public Ramo getRamo() {
		return ramo;
	}

	public void setRamo(Ramo ramo) {
		this.ramo = ramo;
	}

	public List<Ramo> getRamos() {
		return ramos;
	}

	public String getModo() {
		return modo;
	}

	public List<Area> getAreas() {
		
		if(areas ==  null) {
			
			areas = areaRepository.findAll();
		}
		return areas;
	}

	


	
}
