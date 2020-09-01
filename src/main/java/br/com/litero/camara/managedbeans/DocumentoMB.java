package br.com.litero.camara.managedbeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Documento;
import br.com.litero.camara.model.TipoDocumento;
import br.com.litero.camara.persistence.Transactional;
import br.com.litero.camara.repositorios.DocumentoRepoitory;
import br.com.litero.camara.repositorios.TipoDocumentoRepository;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class DocumentoMB implements Serializable{

	private static final long serialVersionUID = 1L;


	private Caso casoAtual;
	@Inject
	private CasoMB casoMB;
	@Inject
	private DocumentoRepoitory documentoRepoitory;
	private List<Documento> documentoList;
	private List<TipoDocumento> tipoDocumentoList;

	@Inject
	private TipoDocumentoRepository tipoDocumentoDAO;
	@Inject
	private FacesMessages messages;

	private TipoDocumento tipoDocumentoSelecionado;

	private Documento documentoAtual;

	private Long documentoId;


	@PostConstruct
	public void construct() {

		init();
	}



	@PreDestroy
	public void destroy() {

		casoAtual = null;
		casoMB = null;
		if( null != tipoDocumentoList) {
			tipoDocumentoList.clear();
			tipoDocumentoList = null;
		}
		if(null != documentoList) {
			documentoList.clear();
			documentoList =  null;
		}
		documentoAtual = null;
		tipoDocumentoSelecionado = null;
		documentoId = null;
	}


	public void init() {

		casoAtual = casoMB.getCaso();
		if(casoAtual != null) {
			documentoList = documentoRepoitory.recuperarDocumentosPorCaso(casoAtual.getCasoId());//baseado no perfil
		}
	}

	public void prepararEditar() {

		if(documentoId == null) {
			throw new RuntimeException("erro na requisição. Este documento não existe");
		}

		documentoAtual =  documentoRepoitory.findBy(documentoId);
	}


	@Transactional
	public void atualiza() {

		documentoAtual =  documentoRepoitory.save(documentoAtual);
		messages.info("Documento atualizado com sucesso");
	}








	@Transactional
	public void adiciona() {

		InputStream stream = null;
		try {
			stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/templates/"+tipoDocumentoSelecionado.getTemplate());
			Documento documento =  new Documento(casoAtual, tipoDocumentoSelecionado);
			documento.gerarTextoModelo(stream);
			documentoRepoitory.save(documento);
			if(documentoList.contains(documento)) {
				documentoList.add(documento);
			}
			messages.info("Documento incluido com sucesso");
			documento = null;
		}finally {
			try {
				stream.close();
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}

	}


	public List<TipoDocumento> getTipoDocumentoList() {
		if(tipoDocumentoList == null) {
			tipoDocumentoList =  tipoDocumentoDAO.findAll();
		}
		return tipoDocumentoList;
	}



	public TipoDocumento getTipoDocumentoSelecionado() {
		return tipoDocumentoSelecionado;
	}



	public void setTipoDocumentoSelecionado(TipoDocumento tipoDocumentoSelecionado) {
		this.tipoDocumentoSelecionado = tipoDocumentoSelecionado;
	}



	public Documento getDocumentoAtual() {
		return documentoAtual;
	}



	public void setDocumentoAtual(Documento documentoAtual) {
		this.documentoAtual = documentoAtual;
	}



	public List<Documento> getDocumentoList() {
		return documentoList;
	}



	public Long getDocumentoId() {
		return documentoId;
	}



	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}






}
