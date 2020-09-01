package br.com.litero.camara.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;
import org.primefaces.json.JSONObject;

import br.com.litero.camara.util.json.JsonMessageType;
import br.com.litero.camara.util.json.Jsonify;

@Entity
@Table(name="caso")
public class Caso implements Jsonify{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="caso_id")
	private Long casoId;
	@NotEmpty
	@Column(name = "descricao", columnDefinition = "VARCHAR(1000)")
	private String descricao;
	@NotEmpty
	@Column(name = "proposta", columnDefinition = "VARCHAR(1000)")
	private String proposta;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date data;
	private BigDecimal valor;
	@Enumerated(EnumType.STRING)
	private StatusCaso status;
	@OneToMany(mappedBy="caso",fetch=FetchType.LAZY,cascade=CascadeType.ALL,orphanRemoval=true)
	private List<Parte>partes;
	@ManyToOne
	@JoinColumn(name="mediador_id",referencedColumnName="mediador_id")
	private Mediador mediador;
	@Enumerated(EnumType.STRING)
	@Column(name="motivo_finalizacao")
	private MotivoFinalizacao motivoFinalizacao;
	private String observacao;
	@Transient
	private String  comentario;
	@Transient
	private MotivoFinalizacao motivoFinalizacaoAux;


	@OneToOne(mappedBy="caso")
	private MediadorCaso mediadorCaso;


	@OneToMany(mappedBy="caso",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<Evento> eventos;





	public Caso() {
		this.partes = new ArrayList<>();
	}

	@PrePersist
	public void prePersist() {
		this.data = new Date();
		this.status = StatusCaso.EM_ANALISE;
	}

	@Transient
	public String getNomesRequerentes() {
		if(this.getRequerentes().isEmpty()) {
			return null;
		}
		List<String> nomes = this.getRequerentes().stream()
				.map(r -> r.getPessoa().getNome())
				.collect(Collectors.toList());
		return String.join(", ", nomes);
	}

	@Transient
	public String getNomesRequeridos() {
		if(this.getRequeridos().isEmpty()) {
			return null;
		}
		List<String> nomes = this.getRequeridos().stream()
				.map(r -> r.getPessoa().getNome())
				.collect(Collectors.toList());
		return String.join(", ", nomes);
	}

	@Transient
	public void removerParte(Parte parte) {
		if(parte != null && this.partes.contains(parte)) {
			//TODO nao pode remover o autor
			//TODO verificar se a parte existe na lista
			this.partes.remove(parte);
			parte.setCaso(null);
			parte =  null;
		}	
	}

	public void adicionarParte(Parte parte) {
		if(this.mediador != null && isMediadorCaso(parte.getPessoa())) {
			throw new RuntimeException("já está defido como mediador no caso");
		}
		Optional<Parte> parteOptional =  partes.stream().filter(p-> p.getPessoa().equals(parte.getPessoa())).findAny();
		if(parteOptional.isPresent()) {
			throw new RuntimeException("já é parte no caso");

		}
		this.partes.add(parte);
	}



	@Transient
	public boolean isAcessivelPor(Usuario usuario) {
		if(usuario == null) {
			throw new RuntimeException("Apenas usuários logados podem ter acesso ao caso");
		}else {
			if(usuario.getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)){
				return true;
			}else if(usuario.getTipoUsuario().equals(TipoUsuario.PARTE)) {
				return isPessoaParteCaso(usuario.getPessoa());
			}else if(usuario.getTipoUsuario().equals(TipoUsuario.MEDIADOR)) {

				return isMediadorCaso(usuario.getPessoa());
			}
		}
		return false;
	}

	@Transient
	public boolean isMediadorCaso(Pessoa other) {
		if(other == null) {
			throw new RuntimeException("Parâmetro inválido");
		}
		return other.equals(this.mediador.getPessoa());
	}

	@Transient
	public boolean isPessoaParteCaso(Pessoa pessoa) {
		Optional<Parte> optional = this.partes.stream().filter(p -> p.getPessoa().equals(pessoa)).findAny();
		return (optional.isPresent()) ? true : false;
	}


	@Transient
	public boolean isAguardandoValidacaoMediador() {

		return this.status.equals(StatusCaso.AGUARDANDO_VALIDACAO_MEDIADOR);
	}






	public void setMotivoFinalizacao(MotivoFinalizacao motivoFinalizacao) {
		this.motivoFinalizacao = motivoFinalizacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public boolean isMediadorDefinido() {

		if(this.getMediador()!=null) {
			return true;
		}
		return false;
	}


	public void aceitarMedidor() {

		this.status = StatusCaso.TRAMITANDO;
	}

	public void aguardarAceiteConvidado() {

		this.status = StatusCaso.AGUARDANDO_ACEITE_CONVIDADO;
	}
	
	
	
	
	public Parte getParte(Pessoa pessoa) {
		
		Parte parte =  null;
		for(Parte p : partes) {
			
			if(p.getPessoa().equals(pessoa)) {
				parte =  p;
				break;
			}
		}
		return parte;	
	}




	/*	public void iniciarTramitacao() {
		if(!this.status.equals(StatusCaso.PROCESSO_COMUNICACAO)) {
			throw new RuntimeException("Não é possível tramitar o caso");
		}
		this.status = StatusCaso.TRAMITANDO;
	}
	 */



	public boolean isCasoAceito() {
		return this.status.equals(StatusCaso.CASO_ACEITO);
	}


	public boolean isTramitando() {
		return this.status.equals(StatusCaso.TRAMITANDO);
	}


	public boolean isProcessoComunicacao() {
		
		return this.status.equals(StatusCaso.PROCESSO_COMUNICACAO);
	}

	@Transient
	public List<Parte> getRequerentes() {
		return this.partes.stream()
				.filter(r->r.getTipoParte()
						.equals(TipoParte.REQUERENTE))
				.collect(Collectors.toList());
	}

	@Transient
	public List<Parte> getRequeridos() {
		return this.partes.stream()
				.filter(r->r.getTipoParte()
						.equals(TipoParte.REQUERIDO))
				.collect(Collectors.toList());
	}

	@Transient
	public boolean isEmAnalise() {
		return this.status.equals(StatusCaso.EM_ANALISE);
	}
	


	@Transient
	public boolean isAguardandoDefinicaoMediador() {
		return this.status.equals(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR);
	}

	@Transient
	public boolean isNegado() {
		return this.status.equals(StatusCaso.NEGADO);
	}

	@Transient
	public boolean isEncerrado() {
		return this.status.equals(StatusCaso.ENCERRADO);
	}

	@Transient
	public boolean isIniciado() {
		return !this.status.equals(StatusCaso.EM_ANALISE) && !this.status.equals(StatusCaso.NEGADO)
				&& !this.status.equals(StatusCaso.ENCERRADO);
	}

	public MotivoFinalizacao getMotivoFinalizacao() {
		return motivoFinalizacao;
	}

	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("casoId", this.casoId);
		json.put("type", JsonMessageType.CASO);
		return json;
	}


	public void setStatus(StatusCaso status) {
		this.status = status;
	}

	public MotivoFinalizacao getMotivoFinalizacaoAux() {
		return motivoFinalizacaoAux;
	}

	public void setMotivoFinalizacaoAux(MotivoFinalizacao motivoFinalizacaoAux) {
		this.motivoFinalizacaoAux = motivoFinalizacaoAux;
	}

	public Mediador getMediador() {
		return mediador;
	}

	public void setMediador(Mediador mediador) {
		this.mediador = mediador;
	}

	public String getObservacao() {
		return observacao;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Long getCasoId() {
		return casoId;
	}

	public void setCasoId(Long casoId) {
		this.casoId = casoId;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getProposta() {
		return proposta;
	}

	public void setProposta(String proposta) {
		this.proposta = proposta;
	}

	public StatusCaso getStatus() {
		return status;
	}
	
	
	
	@Transient
	public List<Evento> getEventosOrdenados(){
		
		
		List<Evento> eventosOrdenados = this.eventos.stream().collect(Collectors.toList());
		eventosOrdenados.sort((e1,e2) -> e1.getData().compareTo(e2.getData()));
		
		return eventosOrdenados;
	}





	public MediadorCaso getMediadorCaso() {
		return mediadorCaso;
	}

	public void setMediadorCaso(MediadorCaso mediadorCaso) {
		this.mediadorCaso = mediadorCaso;
	}

	public List<Parte> getPartes() {
		return partes;
	}

	public void setPartes(List<Parte> partes) {
		this.partes = partes;
	}

	public Set<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(Set<Evento> eventos) {
		this.eventos = eventos;
	}

	
	





}
