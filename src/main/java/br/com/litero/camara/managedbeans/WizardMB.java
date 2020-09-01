package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;

import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Endereco;
import br.com.litero.camara.model.EstadoCivil;
import br.com.litero.camara.model.Parte;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.StatusParte;
import br.com.litero.camara.model.TipoParte;
import br.com.litero.camara.model.TipoPessoa;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.CasoService;
import br.com.litero.camara.service.CepService;
import br.com.litero.camara.service.PessoaService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ConversationScoped
public class WizardMB implements Serializable{


	private static final long serialVersionUID = 1L;


	@Inject
	private UsuarioWeb usuarioWeb;
	@Inject
	private Conversation conversation;
	@Inject
	private PessoaService pessoaService;
	@Inject
	private CasoService casoService;
	@Inject
	private CepService cepService;
	@Size(min=1)
	private List<Parte> requerentes = new ArrayList<>();
	@Size(min=1)
	private List<Parte> requeridos = new ArrayList<>();
	private Pessoa parteSelecionada = new Pessoa();
	private Caso caso = new Caso();
	@Inject
	private FacesMessages messages;


	private static final String DADOS_CASO ="DadosCaso"; 
	private static final String REQUERENTES ="SelecionarPartesCaso"; 
	private static final String REQUERIDOS ="SelecionarPartesContrariasCaso"; 

	private String passoAtual = DADOS_CASO;





	public String iniciarCadastro() {
		if(conversation.isTransient()) {
			conversation.begin();
			Pessoa partePrincipal =  pessoaService.buscarPorId(usuarioWeb.getUsuario().getPessoa().getPessoaId());

			Parte parte =  new Parte(caso,partePrincipal,TipoParte.REQUERENTE);
			parte.setPrincipal(true);
			parte.setStatusParte(StatusParte.ACEITOU_PARTICIPAR_CASO);
			parte.setPessoa(partePrincipal);
			requerentes.add(parte);	
		}
		
		return "DadosCaso?faces-redirect=true";
		
	}


	public String preencherDadosCaso() {

		passoAtual = DADOS_CASO;
		return "DadosCaso?faces-redirect=true";
	}



	public String selecionarRequerentes() {

		passoAtual = REQUERENTES;
		return "SelecionarPartesCaso?faces-redirect=true";
	}



	public String  selecionarRequeridos() {
		passoAtual = REQUERIDOS;
		return "SelecionarPartesContrariasCaso?faces-redirect=true";
	}


	public List<Pessoa> buscarPartes(String filtro){
		return pessoaService.buscarPessoas(filtro);
	}

	
	
	
	

	public void adicionarParte() {

		if(this.jaEstaPresenteNaListaDeRequerentes(parteSelecionada)) {
			messages.error(parteSelecionada.getNome()+" já é Requerente no caso");
			parteSelecionada = null;
		}else {
			if(this.jaEstaPresenteNaListaDeRequeridos(parteSelecionada)) {
				messages.error(parteSelecionada.getNome()+" já é Requerido no caso");
			}else {
				
				
				Parte parte = new Parte(caso,parteSelecionada,TipoParte.REQUERENTE);
				parte.setStatusParte(StatusParte.ACEITOU_PARTICIPAR_CASO);
				parte.setPrincipal(false);
				requerentes.add(parte);
			}
		}
		parteSelecionada = null;
	}


	public String salvarParte() {

		pessoaService.adicionarOuAtualizar(parteSelecionada);

		if(passoAtual.equals(REQUERENTES)) {

			adicionarParte();

		}else if(passoAtual.equals(REQUERIDOS)) {

			adicionarParteContraria();
		}


		return passoAtual+"?faces-redirect=true";
	}

	public String cancelarAcao() {

		parteSelecionada = null;
		return passoAtual+"?faces-redirect=true";
	}



	private boolean jaEstaPresenteNaListaDeRequerentes(Pessoa requerente) {

		boolean presente =  false;
		for(Parte parte : requerentes ) {
			if(parte.getPessoa().equals(requerente)) {
				presente =  true;
				break;
			}
		}
		return presente;
	}


	private boolean jaEstaPresenteNaListaDeRequeridos(Pessoa requerido) {

		boolean presente =  false;
		for(Parte parte : requeridos ) {
			if(parte.getPessoa().equals(requerido)) {
				presente =  true;
				break;
			}
		}
		return presente;
	}




	public void removerParte(Parte parteToRemove) {
		
		
		if(parteToRemove.getPessoa().getPessoaId().equals(usuarioWeb.getUsuario().getPessoa().getPessoaId())) {
			
			messages.error("Não é possível remover o autor do caso");
			
		}else {
		requerentes.remove(parteToRemove);
		}
	}
	
	
	
	public void alterarRequeridoPrincipal(Parte parte) {
		
		
		
		
		for(Parte requerido : requeridos) {
			requerido.setPrincipal(false);
			if(requerido.getPessoa().equals(parte.getPessoa())) {
				requerido.setPrincipal(true);
			}
		}
	}



	public void adicionarParteContraria() {

		if(this.jaEstaPresenteNaListaDeRequeridos(parteSelecionada)) {
			messages.error(parteSelecionada.getNome()+" já é Requerido no caso");
		}else {
			if(this.jaEstaPresenteNaListaDeRequerentes(parteSelecionada)) {
				messages.error(parteSelecionada.getNome()+" já é Requerente no caso");
			}else {
				Parte parte = new Parte(caso,parteSelecionada,TipoParte.REQUERIDO);
			
				
				if(requeridos.isEmpty()) {
					
					parte.setPrincipal(true);
				}else {
					parte.setPrincipal(false);
				}
				requeridos.add(parte);
			}
		}
		parteSelecionada = null;
	}


	public void removerParteContraria(Parte parteContrariaToRemove) {
		requeridos.remove(parteContrariaToRemove);
	}


	public String novaParte() {
		this.parteSelecionada = new Pessoa();
		return "CadastrarParteCaso?faces-redirect=true";
	}
	
	public String novaParteRequerente() {
		this.parteSelecionada = new Pessoa();
		return "CadastrarParteCaso?faces-redirect=true";
	}
	
	public String novaParteRequerido() {
		this.parteSelecionada = new Pessoa();
		return "CadastrarParteCasoSimplificado?faces-redirect=true";
	}
	
	
	public boolean isEmptyRequeridos(){
		
		return this.requeridos.isEmpty();
	}



	public String salvarCaso() {
		
		
		if(this.requeridos.isEmpty()) {
			
			messages.error("Insira no mínimo um requerido");
			
			return null;
		}else {
			
			requerentes.forEach(requerente -> {
			
				caso.getPartes().add(requerente);
			});
			
			requeridos.forEach(requerido -> {
			
				caso.getPartes().add(requerido);
			});
			
		caso = casoService.cadastrar(caso, usuarioWeb.getUsuario());

		if(!conversation.isTransient()) {
			conversation.end();
		}

		return "Recibo?faces-redirec=true";
		}

	}





	public String getPassoAtual() {
		return passoAtual;
	}


	public String validarConversacao() {
		if(conversation.isTransient()) {
			return "Casos?faces-redirec=true";
		}
		return null;
	}



	public void buscarCep() {
		Endereco endereco = cepService.buscarCep(parteSelecionada.getCep());
		parteSelecionada.completarEndereco(endereco);	
	}


	public EstadoCivil[] getEstadosCivis() {
		return EstadoCivil.values();
	}


	public TipoPessoa[] getTiposPessoa() {
		return TipoPessoa.values();
	}


	public Caso getCaso() {
		return caso;
	}



	public List<Parte> getRequerentes() {
		return requerentes;
	}


	public List<Parte> getRequeridos() {
		return requeridos;
	}


	public Pessoa getParteSelecionada() {
		return parteSelecionada;
	}


	public void setParteSelecionada(Pessoa parteSelecionada) {
		this.parteSelecionada = parteSelecionada;
	}


}
