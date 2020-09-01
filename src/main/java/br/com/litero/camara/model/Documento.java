package br.com.litero.camara.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.NumberTool;

@Entity
@Table(name="documento")
public class Documento {

	@Id
	@Column(name="documento_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long documentoId;
	
	private String titulo;
	
	private String codigo;

	private String nome;

	@ManyToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;

	@ManyToOne
	@JoinColumn(name="tipo_documento_id",referencedColumnName="tipo_documento_id")
	private TipoDocumento tipoDocumento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_criacao")
	private Date dataCriacao;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_publicacao")
	private Date dataPublicacao;

	private String texto;
	
	
	@Transient
	public boolean marcadoParaPublicar;
	

	@Deprecated
	public Documento() {

		
	}


	public void gerarTextoModelo(InputStream stream) {

		
			VelocityContext ctx = new VelocityContext();
			Writer writer = new StringWriter();
			ctx.put("caso",this.caso);
			ctx.put("numberTool", new NumberTool());
			ctx.put("locale", new Locale("pt","br"));
			 Reader templateReader = new BufferedReader(new InputStreamReader(stream));
	        Velocity.evaluate(ctx, writer, "log tag name", templateReader);
			this.texto = writer.toString();
	}


	public Documento(Caso caso,TipoDocumento tipoDocumento) {
		this.caso =  caso;
		this.tipoDocumento = tipoDocumento;
		this.nome =  tipoDocumento.getNome();
	}
	
	
	
	public void publicar(){
		if(this.dataPublicacao == null) {
		Date hoje = new Date();
		this.dataPublicacao = hoje;
		this.codigo = String.valueOf(hoje.getTime());
		}else {
			throw new RuntimeException("Documento já publicado");
		}
	}
	
	
	@Transient
	public boolean isPublicado() {
		
		return this.dataPublicacao != null;
	}
	
	
	
	

	public void setCodigo(String codigo) {
		
	}


	@PrePersist
	public void prePresist() {

		this.dataCriacao = new Date();
	}


	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public Long getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Caso getCaso() {
		return caso;
	}

	public void setCaso(Caso caso) {
		this.caso = caso;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}



	public String getTitulo() {
		return titulo;
	}




	public String getCodigo() {
		return codigo;
	}




	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}




	public boolean isMarcadoParaPublicar() {
		return marcadoParaPublicar;
	}




	public void setMarcadoParaPublicar(boolean marcadoParaPublicar) {
		this.marcadoParaPublicar = marcadoParaPublicar;
	}




	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}



}


