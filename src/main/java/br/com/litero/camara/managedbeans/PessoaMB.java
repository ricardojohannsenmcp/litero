package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.litero.camara.model.Endereco;
import br.com.litero.camara.model.EstadoCivil;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.TipoPessoa;
import br.com.litero.camara.service.CepService;
import br.com.litero.camara.service.PessoaService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class PessoaMB implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	

	private static final String EDICAO = "edicao";
	private static final String LISTAGEM = "lista";

	private String modo = LISTAGEM;	



	@Inject
	private PessoaService pessoaService;
	
	
	@Inject
	private CepService cepService;
	
	
	private List<Pessoa> partes;
	
	private Pessoa parte;
	
	@Inject
	private FacesMessages messages;
	
	
	
	public void listar() {
		
		this.partes = pessoaService.recuperarTodos();
		this.parte = new Pessoa();
		modo = LISTAGEM;
	}
	
	
	
	public void novaPessoa() {
		
		this.parte = new Pessoa();
		modo = EDICAO;
		
	}
	
	
	
	public void salvar() {
		
		this.parte =  pessoaService.adicionarOuAtualizar(parte);
		
		this.partes = pessoaService.recuperarTodos();
		modo = LISTAGEM;
		
		messages.info("Dados salvos com sucesso");
	}
	
	
	public void cancelar() {
		
		this.parte =  null;
		
		modo = LISTAGEM;
	}
	
	
	

	public void buscarCep() {
		Endereco endereco = cepService.buscarCep(parte.getCep());
		parte.completarEndereco(endereco);	
	}
	
	public boolean isModoListagem() {

		return this.modo.equals(LISTAGEM);
	}

	public boolean isModoEdicao() {

		return this.modo.equals(EDICAO);
	}
	
	
	
	public void removerParte(Pessoa parte) {
		 try {
			 
			
			pessoaService.remover(parte);
			messages.info("Pessoa removida com sucesso");
			listar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messages.error("Pessoa não pode ser removida");
		}
	}
	
	
	public void editarParte(Pessoa parte) {
		
		this.parte = parte;
		modo = EDICAO;
	}

	
	

	public List<Pessoa> buscarPartes(String filtro){
		return pessoaService.buscarPessoas(filtro);
	}
	


	public Pessoa getParte() {
		return parte;
	}
	
	
	


	public EstadoCivil[] getEstadosCivis() {
		return EstadoCivil.values();
	}


	public TipoPessoa[] getTiposPessoa() {
		return TipoPessoa.values();
	}



	public List<Pessoa> getPartes() {
		return partes;
	}


	
	
	
	
	
	

}
