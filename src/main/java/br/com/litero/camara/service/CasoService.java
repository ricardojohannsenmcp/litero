package br.com.litero.camara.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import br.com.litero.camara.exceptions.NegocioException;
import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Cobranca;
import br.com.litero.camara.model.Evento;
import br.com.litero.camara.model.Mediador;
import br.com.litero.camara.model.MediadorCaso;
import br.com.litero.camara.model.MotivoFinalizacao;
import br.com.litero.camara.model.Notificacao;
import br.com.litero.camara.model.Parte;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.StatusCaso;
import br.com.litero.camara.model.StatusMediador;
import br.com.litero.camara.model.TipoEvento;
import br.com.litero.camara.model.TipoParte;
import br.com.litero.camara.model.TipoUsuario;
import br.com.litero.camara.model.Usuario;
import br.com.litero.camara.repositorios.CasoRepository;
import br.com.litero.camara.repositorios.CobrancaRepository;
import br.com.litero.camara.repositorios.EventoRepository;
import br.com.litero.camara.repositorios.MediadorCasoRepository;
import br.com.litero.camara.repositorios.NotificacaoRepository;

@ApplicationScoped
public class CasoService {



	@Inject
	private CasoRepository casoRepository;
	@Inject
	private EventoRepository eventoRepository;

	@Inject
	private NotificacaoRepository notificacaoRepository;
	
	
	@Inject
	private CobrancaRepository cobrancaRepository;
	
	@Inject
	private MediadorCasoRepository mediadorCasoRepository;






	@Transactional
	public Caso cadastrar(Caso caso, Usuario usuario) {

	
		if(caso.getRequerentes() == null || caso.getRequerentes().isEmpty()) {
			throw new NegocioException("O caso deve conter no mínimo um requerente");
		}
		if(caso.getRequeridos() == null || caso.getRequeridos().isEmpty()) {
			throw new NegocioException("O caso deve conter no mínimo um requerido");
		}
		/*requerentes.forEach(requerente -> {
			Parte parte =  new Parte(caso, requerente, TipoParte.REQUERENTE);
			parte.setConfirmado(true);
			if(requerente.equals(usuario.getPessoa())) {
				
			}
			
			caso.getPartes().add(parte);
		});
		requeridos.forEach(requerente -> {
			Parte parte =  new Parte(caso, requerente, TipoParte.REQUERIDO);
			parte.setConfirmado(false);
			caso.getPartes().add(parte);
		});*/
		casoRepository.save(caso);
		Evento evento = new Evento(caso, TipoEvento.PROTOCOLO, null, usuario);
		eventoRepository.save(evento);
		return caso;
	}




	@Transactional
	public Caso negar(Caso caso) {

		Caso casoOriginal =  casoRepository.findBy(caso.getCasoId());
		if(!casoOriginal.getStatus().equals(StatusCaso.EM_ANALISE)) {
			throw new NegocioException("O caso já foi aceito");
		}
		if(caso.getComentario() == null || caso.getComentario().isEmpty()) {
			throw new NegocioException("Informe o motivo pelo qual o caso está sendo negado");
		}
		casoOriginal.setStatus(StatusCaso.NEGADO);
		casoOriginal.setObservacao(caso.getComentario());
		casoRepository.save(casoOriginal);
		Evento evento = new Evento(caso, TipoEvento.CASO_NEGADO, caso.getObservacao(),null);
		eventoRepository.save(evento);
		List<Parte> requerentes = casoOriginal.getRequerentes();
		requerentes.forEach(requerente -> {
			Notificacao notificacao = new Notificacao(casoOriginal, requerente.getPessoa(), "O caso #"+casoOriginal.getCasoId()+" foi negado");
			notificacaoRepository.save(notificacao);
		});
		return casoOriginal;
	}


	@Transactional
	public Caso aceitar(Caso caso,Usuario usuario) {

		Caso casoOriginal =  casoRepository.findBy(caso.getCasoId());
		if(!casoOriginal.getStatus().equals(StatusCaso.EM_ANALISE)) {
			throw new NegocioException("O caso já foi aceito");
		}
		
		
		for(Parte parte : casoOriginal.getRequerentes()) {
			
			List<Cobranca> cobrancas = cobrancaRepository.recuperarCobrancasPorCaso(casoOriginal.getCasoId(), parte.getPessoa().getPessoaId());
			if(cobrancas.isEmpty()) {
				throw new NegocioException("Antes de aceitar o caso gere informação de cobrança para os requerentes");

			}
		}
		
		
		
		
		casoOriginal.setStatus(StatusCaso.CASO_ACEITO);
		casoRepository.save(casoOriginal);
		Evento evento = new Evento(caso, TipoEvento.CASO_ACEITO, "", usuario);
		eventoRepository.save(evento);
		List<Parte> requerentes = casoOriginal.getRequerentes();
		requerentes.forEach(requerente -> {
			Notificacao notificacao = new Notificacao(casoOriginal, requerente.getPessoa(), "O caso #"+casoOriginal.getCasoId()+" foi aceito");
			notificacaoRepository.save(notificacao);
		});
		return casoOriginal;
	}
	
	
	
	
	@Transactional
	public Caso processoDeComunicacao(Caso caso) {
		
		
		Caso casoOriginal =  casoRepository.findBy(caso.getCasoId());
		List<Cobranca> cobrancas =  cobrancaRepository.recuperarCobrancasPorCaso(casoOriginal.getCasoId());
		
		for(Cobranca cobranca : cobrancas) {
			
			if(cobranca.getDataPagamento() == null) {
				
				throw new NegocioException("Existem cobranças ainda não pagas");
			}
		}
		
		casoOriginal.setStatus(StatusCaso.PROCESSO_COMUNICACAO);
		casoRepository.save(casoOriginal);
		
		return casoOriginal;
		
	}




	@Transactional
	public Caso finalizar(Caso caso,MotivoFinalizacao motivoFinalizacao,Usuario usuario) {

		if(motivoFinalizacao == null) {
			throw new NegocioException("Informe o motivo pelo qual o caso está sendo finalizado");
		}
		if(caso.getComentario() == null || caso.getComentario().isEmpty()) {
			throw new NegocioException("Descreva o motivo pelo qual o caso está sendo finalizado");
		}
		Caso casoOriginal =  casoRepository.findBy(caso.getCasoId());
		if(casoOriginal.getStatus().equals(StatusCaso.EM_ANALISE) || casoOriginal.getStatus().equals(StatusCaso.ENCERRADO)){
			throw new NegocioException("O caso não pode ser encerrado");
		}
		casoOriginal.setStatus(StatusCaso.ENCERRADO);
		casoOriginal.setMotivoFinalizacao(motivoFinalizacao);
		casoOriginal.setObservacao(caso.getComentario());
		casoRepository.save(casoOriginal);
		Evento evento = new Evento(caso, TipoEvento.CASO_ENCERRADO, caso.getObservacao(),usuario);
		eventoRepository.save(evento);
		List<Parte> partes = casoOriginal.getPartes();
		partes.forEach(parte->{
			//verificar se a parte aceitou participação
			Notificacao notificacao = new Notificacao(casoOriginal, parte.getPessoa(), "O caso #"+casoOriginal.getCasoId()+" foi finalizado");
			notificacaoRepository.save(notificacao);
			
		});
		return casoOriginal;
	}


	@Transactional
	public Caso definirMediador(Caso caso,Mediador mediador,Usuario usuario) {

		if(mediador == null) {
			throw new NegocioException("O mediador não pode ser nulo");
		}
		Caso casoOriginal =  casoRepository.findBy(caso.getCasoId());
		if(!casoOriginal.getStatus().equals(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR)) {
			throw new NegocioException("A etapa de definição do mediador não está liberada");
		}
		
		
		MediadorCaso mediadorCaso =  new MediadorCaso(mediador, casoOriginal,StatusMediador.AGUARDANDO_APROVACAO);
		caso.setMediadorCaso(mediadorCaso);
		
		
		mediadorCasoRepository.save(mediadorCaso);
		
		//casoOriginal.setMediador(mediador);
		
		
		casoOriginal.setStatus(StatusCaso.AGUARDANDO_VALIDACAO_MEDIADOR);
		casoRepository.save(casoOriginal);
		Evento evento = new Evento(casoOriginal,TipoEvento.INCLUSAO_ESPECIALISTA,"",usuario);
		eventoRepository.save(evento);
		List<Parte> partes = casoOriginal.getPartes();
		partes.forEach(parte->{
			//verificar se a parte aceitou participação
			Notificacao notificacao = new Notificacao(casoOriginal, parte.getPessoa(), "O mediador para o  caso #"+casoOriginal.getCasoId()+" foi definido");
			notificacaoRepository.save(notificacao);
			
		});
		return casoOriginal;
	}


	@Transactional
	public Caso delegarEscolhaMediador(Caso caso,Usuario usuario) {

		Caso casoOriginal = casoRepository.findBy(caso.getCasoId());
		if(!casoOriginal.getStatus().equals(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR)) {
			throw new NegocioException("A etapa de definição do mediador não está liberada");
		}
		
		casoOriginal.setStatus(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR_CAMARA);
		casoRepository.save(casoOriginal);
		Evento evento = new Evento(casoOriginal,TipoEvento.INCLUSAO_ESPECIALISTA,"A definição do mediador foi delegada para a câmara",usuario);
		eventoRepository.save(evento);
		return casoOriginal;

	}

	@Transactional
	public Caso aprovarMediador(Caso caso,Usuario usuario) {

		Caso casoOriginal = casoRepository.findBy(caso.getCasoId());
		if(!casoOriginal.getStatus().equals(StatusCaso.AGUARDANDO_VALIDACAO_MEDIADOR)) {
			throw new NegocioException("Fase de definição do mediador ainda não foi liberada ");
		}
		if(casoOriginal.getMediadorCaso().getMediador() == null) {
			throw new NegocioException("O mediador ainda não foi definido para o caso");
		}
		
		//TODO falta implementar checar se o usuario é realmente a parte que deve aprovar o mediador e tem que estar participando do caso
		
	MediadorCaso mediadorCaso = casoOriginal.getMediadorCaso();
		
		mediadorCaso.setStatusMediador(StatusMediador.APROVADO);
		
		mediadorCasoRepository.save(mediadorCaso);
		
		
		casoOriginal.setMediador(caso.getMediadorCaso().getMediador());//ok

    casoOriginal.setStatus(StatusCaso.TRAMITANDO);
    casoRepository.save(casoOriginal);
    Evento evento = new Evento(casoOriginal,TipoEvento.APROVACAO_MEDIADOR,"",usuario);
	eventoRepository.save(evento);
	List<Parte> partes = casoOriginal.getPartes();
	partes.forEach(parte->{
		//verificar se a parte aceitou participação
		Notificacao notificacao = new Notificacao(casoOriginal, parte.getPessoa(), "A escolha do mediador para o  caso #"+casoOriginal.getCasoId()+" foi aprovada");
		notificacaoRepository.save(notificacao);
		
	});
	
	Notificacao notificacao = new Notificacao(casoOriginal, casoOriginal.getMediador().getPessoa(), "Você foi escolhido como mediador para o  caso #"+casoOriginal.getCasoId());
	notificacaoRepository.save(notificacao);

	
	
    return casoOriginal;
	}
	
	
	@Transactional
	public Caso reprovarMediador(Caso caso,Usuario usuario) {

		Caso casoOriginal = casoRepository.findBy(caso.getCasoId());
		if(!casoOriginal.getStatus().equals(StatusCaso.AGUARDANDO_VALIDACAO_MEDIADOR)) {
			throw new NegocioException("Fase de definição do mediador ainda não foi liberada ");
		}
		if(casoOriginal.getMediador() == null) {
			throw new NegocioException("O mediador ainda não foi definido para o caso");
		}
		
		
		
		//TODO falta implementar checar se o usuario é realmente a parte que deve aprovar o mediador e tem que estar participando do caso

		MediadorCaso mediadorCaso = casoOriginal.getMediadorCaso();
		
		mediadorCaso.setStatusMediador(StatusMediador.REJEITADO);
		
		mediadorCasoRepository.save(mediadorCaso);
		
		
    casoOriginal.setStatus(StatusCaso.IMPASSE);
    casoRepository.save(casoOriginal);
    Evento evento = new Evento(casoOriginal,TipoEvento.REPROVACAO_MEDIADOR,"Existe um impasse na definição do mediador, neste caso o mesmo será definido pela câmara",usuario);
	eventoRepository.save(evento);
	List<Parte> partes = casoOriginal.getPartes();
	partes.forEach(parte->{
		//verificar se a parte aceitou participação
		Notificacao notificacao = new Notificacao(casoOriginal, parte.getPessoa(), "A escolha do mediador para o  caso #"+casoOriginal.getCasoId()+" foi rejeitada");
		notificacaoRepository.save(notificacao);
		
	});
    return casoOriginal;
	}
	
	@Transactional
	public Caso enviarConviteMediadorConvidado(Caso caso,Mediador mediador,Usuario usuario) {
		
		Caso casoOriginal = casoRepository.findBy(caso.getCasoId());
		
		if(!casoOriginal.getStatus().equals(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR)) {
			
			throw new NegocioException("Fase de definição do mediador ainda não foi liberada ");
		}
		
		casoOriginal.setStatus(StatusCaso.AGUARDANDO_ACEITE_CONVIDADO);
		casoRepository.save(casoOriginal);
		Evento evento = new Evento(casoOriginal,TipoEvento.CONVITE_MEDIADOR,"",usuario);
		eventoRepository.save(evento);
		List<Parte> partes = casoOriginal.getPartes();
		partes.forEach(parte->{
			//verificar se a parte aceitou participação
			Notificacao notificacao = new Notificacao(casoOriginal, parte.getPessoa(), "O convite para o mediador do  caso #"+casoOriginal.getCasoId()+" será enviado");
			notificacaoRepository.save(notificacao);
			
		});
		
		return casoOriginal;
	}
	
	
	
	@Transactional
	public Caso convidadoAceitaCaso(Caso caso, Mediador mediador) {
		
		Caso casoOriginal = casoRepository.findBy(caso.getCasoId());
		if(!casoOriginal.getStatus().equals(StatusCaso.AGUARDANDO_ACEITE_CONVIDADO)) {
			throw new NegocioException("Fase  não foi liberada ");
		}
		casoOriginal.setMediador(mediador);
		casoOriginal.setStatus(StatusCaso.AGUARDANDO_VALIDACAO_MEDIADOR);
		casoRepository.save(casoOriginal);
		Evento evento = new Evento(casoOriginal,TipoEvento.RESPOSTA_CONVITE,"O mediador convidado aceitou participar do caso",null);
		eventoRepository.save(evento);
		List<Parte> partes = casoOriginal.getPartes();
		partes.forEach(parte->{
			//verificar se a parte aceitou participação
			Notificacao notificacao = new Notificacao(casoOriginal, parte.getPessoa(), "O mediador convidado caso #"+casoOriginal.getCasoId()+" aceitou o convite");
			notificacaoRepository.save(notificacao);
			
		});
		return casoOriginal;
	}
	
	
	
	@Transactional
	public Caso convidadoRejeitaCaso(Caso caso, Mediador mediador) {
		
		Caso casoOriginal = casoRepository.findBy(caso.getCasoId());
		if(!casoOriginal.getStatus().equals(StatusCaso.AGUARDANDO_ACEITE_CONVIDADO)) {
			throw new NegocioException("Fase  não foi liberada ");
		}
		casoOriginal.setMediador(null);
		casoOriginal.setStatus(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR_CAMARA);
		casoRepository.save(casoOriginal);
		Evento evento = new Evento(casoOriginal,TipoEvento.RESPOSTA_CONVITE,"O mediador convidado não aceitou participar do caso devendo então esta escolha ficar por conta da câmara",null);
		eventoRepository.save(evento);
		List<Parte> partes = casoOriginal.getPartes();
		partes.forEach(parte->{
			//verificar se a parte aceitou participação
			Notificacao notificacao = new Notificacao(casoOriginal, parte.getPessoa(), "O mediador convidado caso #"+casoOriginal.getCasoId()+" rejeitou o convite");
			notificacaoRepository.save(notificacao);
			
		});
		return casoOriginal;
	}
	
	
	
	
	public Caso forcarDefinicaoMediador(Caso caso, Mediador mediador,Usuario usuario) {
		
		Caso casoOriginal = casoRepository.findBy(caso.getCasoId());
		
		if(!usuario.getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
			
			throw new NegocioException("Você não tem permissão para executar esta ação");
		}
		
		if(!casoOriginal.getStatus().equals(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR_CAMARA)  && !casoOriginal.getStatus().equals(StatusCaso.IMPASSE) ) {
			throw new NegocioException("Operação não permitida nesta fase ");
		}
		
		casoOriginal.setMediador(mediador);
		casoOriginal.setStatus(StatusCaso.TRAMITANDO);
		casoRepository.save(casoOriginal);
		Evento evento = new Evento(casoOriginal,TipoEvento.INCLUSAO_ESPECIALISTA,"Mediador escolhido pela câmara",null);
		eventoRepository.save(evento);
		List<Parte> partes = casoOriginal.getPartes();
		partes.forEach(parte->{
			//verificar se a parte aceitou participação
			Notificacao notificacao = new Notificacao(casoOriginal, parte.getPessoa(), "O mediador convidado caso #"+casoOriginal.getCasoId()+" rejeitou o convite");
			notificacaoRepository.save(notificacao);
			
		});
		return casoOriginal;
		
	}
	
	
	






	public List<Caso>buscarCasosDoUsuario(Long usuarioId){
		return casoRepository.buscarCasosDoUsuario(usuarioId);
	}

	public List<Caso> buscarCasoFiltroParte(Long idCaso, Enum<StatusCaso> status, Date dataCriacao, String nomeParticipante, String cpfCnpjParticipante, String cpfCnpjParte){
		return casoRepository.buscarCasoFiltroParte(idCaso, status, dataCriacao, nomeParticipante, cpfCnpjParticipante, cpfCnpjParte);
	}

	public List<Caso>buscarCasosDoColaborador(String cpf){
		//return casoDao.buscarCasoDoColaborador(cpf);
		return null;
	}

	public List<Caso> buscarCasoFiltroColaborador(Long idCaso, Enum<StatusCaso> status, Date dataCriacao, String nomeParticipante, String cpfCnpjParticipante, String cpfColaborador){
		//return casoDao.buscarCasoFiltroColaborador(idCaso, status, dataCriacao, nomeParticipante, cpfCnpjParticipante, cpfColaborador);

		return null;
	}

	public List<Caso> buscarPorResponsavel(){
		return casoRepository.buscarCasoDoResponsavel();
	}

	public List<Caso> buscarCasoFiltroAdministrador(Long idCaso, Enum<StatusCaso> status, Date dataCriacao, String nomeParticipante, String cpfCnpjParticipante){
		return casoRepository.buscarCasoFiltroAdministrador(idCaso, status, dataCriacao, nomeParticipante, cpfCnpjParticipante);
	}

	public Caso recuperarCasoPorId(Long casoId) {
		return casoRepository.findBy(casoId);
	}


	/**
	 * Vem carregado com as coleções
	 * **/
	public Caso recuperar(Long casoId) {
		return casoRepository.recuperarCaso(casoId);
	}


	/*	@Transactional
	public Caso cadastrarCaso(Caso caso, Usuario usuario) {
		if(caso.getRequerentes() == null || caso.getRequerentes().isEmpty()) {
			throw new RuntimeException("O caso deve possuir no mínimo um requerente");
		}
		if(caso.getRequeridos() == null || caso.getRequeridos().isEmpty()) {
			throw new RuntimeException("O caso deve possuir no mínimo um requerido");
		}
		caso = casoDao.adiciona(caso);
		Evento evento = new Evento(caso, TipoEvento.PROTOCOLO, null, usuario);
		eventoDAO.adiciona(evento);
		return caso;
	}
	 */
	

	public Long recuperarQuantidadeCasoPorStatus(Enum<StatusCaso> status) {
		return casoRepository.recuperarQuantidadeCasoPorStatus(status);
	}

	public Long recuperarQuantidadeCaso() {
		return casoRepository.recuperarQuantidadeCaso();
	}

	public Caso recuperarCasoComColaboradores(Long casoId) {
		//return casoDao.recuperarCasoComColaboradores(casoId);

		return null;
	}




	/*	@Transactional
	public Caso definirEspecialista(Long casoId,Colaborador colaborador) {
		Caso caso = recuperar(casoId);
		caso.setColaborador(colaborador);
		caso.iniciarTramitacao();
		Evento evento = new Evento(caso, TipoEvento.INCLUSAO_ESPECIALISTA, caso.getColaborador()
				.getPessoa().getNome()+" foi defnido como mediador do caso",null);
		eventoDAO.adiciona(evento);
		return caso;
	}*/




}
