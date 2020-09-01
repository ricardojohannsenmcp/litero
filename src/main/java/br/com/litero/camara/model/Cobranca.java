package br.com.litero.camara.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import org.hibernate.validator.constraints.NotEmpty;

import com.sun.istack.internal.NotNull;

@Entity
@Table(name="cobranca")
public class Cobranca {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="cobranca_id")
	private Long cobrancaId;
	@ManyToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="pessoa_id",referencedColumnName="pessoa_id")
	private Pessoa pessoa;
	@Enumerated(EnumType.STRING)
	private TipoCobranca tipoCobranca;
	
	@NotNull
	@Column(name="valor_cobranca")
	private BigDecimal valorCobranca;
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name="data_vencimento")
	private Date dataVencimento;
	@Temporal(TemporalType.DATE)
	@Column(name="data_pagamento")
	private Date dataPagamento;
	@Column(name="valor_recebido")
	private BigDecimal valorRecebido;
	
	@Column(name="valor_desconto")
	private BigDecimal valorDesconto;

	@Enumerated(EnumType.STRING)
	private StatusCobranca statusCobranca;

	@NotEmpty
	private String boleto; 


	@Transient
	public BigDecimal getTotal() {

		return this.valorCobranca;
	}





	public Cobranca() {
		
		this(null);
	}





	public Cobranca(Caso caso) {

		this.caso = caso;
		this.statusCobranca =  StatusCobranca.EM_ABERTO;
	}

	@PrePersist
	public void prePersist() {

		this.data =  new Date();
	}


	public Long getCobrancaId() {
		return cobrancaId;
	}
	public void setCobrancaId(Long cobrancaId) {
		this.cobrancaId = cobrancaId;
	}
	public Caso getCaso() {
		return caso;
	}
	public void setCaso(Caso caso) {
		this.caso = caso;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
	public BigDecimal getValorCobranca() {
		return valorCobranca;
	}
	public void setValorCobranca(BigDecimal valorCobranca) {
		this.valorCobranca = valorCobranca;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public BigDecimal getValorRecebido() {
		return valorRecebido;
	}
	public void setValorRecebido(BigDecimal valorRecebido) {
		this.valorRecebido = valorRecebido;
	}
	public String getBoleto() {
		return boleto;
	}
	public void setBoleto(String boleto) {
		this.boleto = boleto;
	}


	public StatusCobranca getStatusCobranca() {
		return statusCobranca;
	}


	public void setStatusCobranca(StatusCobranca statusCobranca) {
		this.statusCobranca = statusCobranca;
	}





	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}





	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}







}
