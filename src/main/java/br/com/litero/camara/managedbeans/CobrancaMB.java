package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Cobranca;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.StatusCaso;
import br.com.litero.camara.model.StatusCobranca;
import br.com.litero.camara.model.TipoUsuario;
import br.com.litero.camara.persistence.Transactional;
import br.com.litero.camara.repositorios.CasoRepository;
import br.com.litero.camara.repositorios.CobrancaRepository;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.util.arquivos.FileSaver;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class CobrancaMB implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Inject
	private CasoMB casoMB;
	private Caso casoAtual;
	private List<Cobranca> cobrancaList;
	
	private Cobranca cobrancaAtual;
	@Inject
	private CobrancaRepository cobrancaRepository;
	@Inject
	private UsuarioWeb usuarioWeb;
	@Inject
	private CasoRepository casoRepository;
	@Inject
	private FacesMessages messages;
	
	
	@Inject
	private FileSaver fileSaver;
	
	
	

	
	
	
	private Long casoId;
	
	private Long cobrancaId;
	

	
	public void init() {
		
		casoAtual = casoMB.getCaso();
		
		if(usuarioWeb.getUsuario().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
			cobrancaList = cobrancaRepository.recuperarCobrancasPorCaso(casoAtual.getCasoId());
		}
		else {
			cobrancaList = cobrancaRepository.recuperarCobrancasPorCaso(casoAtual.getCasoId(), usuarioWeb.getUsuario().getPessoa().getPessoaId());
		}
		
		
		
		
		
		if(casoAtual.getStatus().equals(StatusCaso.CASO_ACEITO)) {
			Optional<Cobranca> cobrancaOptional = cobrancaList.stream().filter(c -> c.getDataPagamento() == null).findFirst();
				if(cobrancaOptional.isPresent()) {
					
					cobrancaAtual = cobrancaOptional.get();
					
					if(usuarioWeb.getUsuario().getPessoa().equals(cobrancaAtual.getPessoa())) {
						messages.info("Você possui cobranças em aberto, o caso prosseguirá após o pagamento");
					}
				}	
		}

	}
	
	@PreDestroy
	public void destroy() {
		
		casoAtual = null;
		casoMB = null;
		if(null != cobrancaList) {
			cobrancaList.clear();
			cobrancaList = null;
		}
		cobrancaAtual = null;
		casoId = null;
		
	}
	
	
	

	
	
	public void setup() {
		
		
		if(cobrancaId != null) {
			
			cobrancaAtual = cobrancaRepository.findBy(cobrancaId);
			
			
			if(cobrancaAtual == null) {
				
				throw new RuntimeException("Erro de requisição");
			}
			
			casoAtual = casoRepository.recuperarCaso(cobrancaAtual.getCaso().getCasoId());
					
					cobrancaId = null;
			
		}else {
		
		if(casoId == null) {
			throw new RuntimeException("Erro de requisição");
		}
		casoAtual = casoRepository.findBy(casoId);
		cobrancaAtual = new Cobranca(casoAtual);
		casoId = null;
		}
		
	}
	
	
	public String novaCobranca() {
		
		return "Cobranca?faces-redirect=true&includeViewParams=true";
	}
	
	
	public void uploadBoleto(FileUploadEvent event) {
		
		
	
		String finalFileName =  fileSaver.write(event.getFile().getFileName(), event.getFile().getContents());
		cobrancaAtual.setBoleto(finalFileName);
		

	
	}
	
	
	
	
	@Transactional
	public String salvar() {
		try {
			if(cobrancaAtual.getPessoa()==null) {
				messages.error("selecione a parte antes de salvar.");
			}
			else if(cobrancaAtual.getValorCobranca()==null) {
				messages.error("preencha o valor antes de salvar.");
			}
			else if(cobrancaAtual.getBoleto()==null) {
				messages.error("faça upload do boleto antes de salvar.");
			}
			else {
				cobrancaRepository.save(cobrancaAtual);
				messages.info("Cobrança salva com sucesso.");
				
				return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
			}
			
			return "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messages.error(e.getMessage());
			return null;
		}
	}
	
	
	public List<Pessoa> getPartes(){
		return casoAtual.getPartes().stream().map(p -> p.getPessoa()).collect(Collectors.toList());
	}


	public Cobranca getCobrancaAtual() {
		return cobrancaAtual;
	}


	public void setCobrancaAtual(Cobranca cobrancaAtual) {
		this.cobrancaAtual = cobrancaAtual;
	}


	public List<Cobranca> getCobrancaList() {
		return cobrancaList;
	}


	public Caso getCasoAtual() {
		
		return casoAtual;
	}


	public void setCasoId(Long casoId) {
		this.casoId = casoId;
	}


	public Long getCasoId() {
		return casoId;
	}

	public UsuarioWeb getUsuarioWeb() {
		return usuarioWeb;
	}
	
	public StatusCobranca[] getStatusCobranca() {
		return StatusCobranca.values();
	}

	public Long getCobrancaId() {
		return cobrancaId;
	}

	public void setCobrancaId(Long cobrancaId) {
		this.cobrancaId = cobrancaId;
	}

	

}
