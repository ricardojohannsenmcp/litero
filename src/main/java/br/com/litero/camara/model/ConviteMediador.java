package br.com.litero.camara.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="convite_mediador")
public class ConviteMediador {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="convite_id")
	private Long conviteId;

	@ManyToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;


	@ManyToOne
	@JoinColumn(name="pessoa_id",referencedColumnName="pessoa_id")
	private Pessoa pessoa;


	private String token;
	
	
	
	
	
	
	

	public ConviteMediador() {
		
	}



	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_expiracao")
	private Date dataExpiracao;










	public ConviteMediador(Caso caso, Pessoa pessoa) {
		super();
		this.caso = caso;
		this.pessoa = pessoa;
		this.data = new Date();

		Calendar expiracao = Calendar.getInstance();
		expiracao.setTime(data);
		expiracao.add(Calendar.DAY_OF_MONTH,2);

		token =  UUID.randomUUID().toString();
	}



//TODO melhorar esse método
	public boolean isExpirado() {

		Calendar hoje = Calendar.getInstance();
		Calendar expiracao = Calendar.getInstance();
		long diff = hoje.getTime().getTime() - expiracao.getTime().getTime();
		long dias = TimeUnit.MILLISECONDS.toDays(diff);
		return dias > 2;

	}



	public Long getConviteId() {
		return conviteId;
	}

	public Caso getCaso() {
		return caso;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public String getToken() {
		return token;
	}

	public Date getData() {
		return data;
	}

	public Date getDataExpiracao() {
		return dataExpiracao;
	}



	public void setToken(String token) {
		this.token = token;
	}












}
