package br.com.litero.camara.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import br.com.litero.camara.model.Mensagem;
import br.com.litero.camara.repositorios.MensagemRepository;

@RequestScoped
public class MensagemService {
	
	
	@Inject
	private MensagemRepository mensagemRepository;
	
	
	
	
	@Transactional
	public Mensagem adiciona(Mensagem mensagem) {
	mensagemRepository.save(mensagem);
		return mensagem;
	}
	
	
/*	@Transactional
	public Mensagem salvarMensagemComAnexo(Caso caso,Pessoa pessoa,String originalFileName,String finalFileName) {
		
		Mensagem mensagem = new Mensagem(caso,pessoa,originalFileName,finalFileName);
		mensagemRepository.adiciona(mensagem);
		return mensagem;
	}*/
	
	
	public List<Mensagem> recuperarMensagensPorAnexoPorCaso(Long casoId){
		
		return mensagemRepository.recuperarMensagensComAnexoPorCaso(casoId);
	}
	
	
	public List<Mensagem>recuperarMensagensPorCaso(long casoId){
		return mensagemRepository.recuperarMensagensPorCaso(casoId);
	}

}
