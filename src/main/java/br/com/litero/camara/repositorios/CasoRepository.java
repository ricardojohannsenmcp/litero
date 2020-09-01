package br.com.litero.camara.repositorios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Parte;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.StatusCaso;
import br.com.litero.camara.model.StatusMediador;
import br.com.litero.camara.model.TipoParte;

@Repository
public abstract class CasoRepository extends AbstractEntityRepository<Caso, Long>{
	

	
	
	public List<Caso> buscarCasosDoUsuario(Long usuarioId){
		return typedQuery("select c from Caso c, Parte cp, Pessoa p where cp.caso.casoId = c.casoId and cp.pessoa.pessoaId = p.pessoaId and p.usuario.usuarioId =:usuarioId")
				.setParameter("usuarioId", usuarioId)
				.getResultList();
	}
	
	
	
	
	public List<Caso> buscarCasoFiltroParte(Long idCaso, Enum<StatusCaso> status, Date dataCriacao, String nomeParticipante, String cpfCnpjParticipante, String cpfCnpjParte){
		
		Map<String,Object>parametros = new HashMap<>();
		
		StringBuilder jpql = new StringBuilder("select c.casoId,c.valor,c.descricao,c.data,c.status,pa.pessoaId,pa.nome,p.tipoParte from Caso c left join c.partes p join p.pessoa pa where c.casoId is not null and pa.cpfCnpj like :cpfCnpjParte");
		
		parametros.put("cpfCnpjParte", cpfCnpjParte.toUpperCase());
		
		
		if(idCaso!=null) {
			parametros.put("casoId", idCaso);
			jpql.append(" and c.casoId = :casoId");
		}
		else {
			
			if(status!=null) {
				parametros.put("statusCaso", status);
				jpql.append(" and c.status = :statusCaso");
			}
			
			if(dataCriacao!=null){
				parametros.put("dataCriacao", dataCriacao);
				jpql.append(" and DATE_FORMAT(c.data, '%d%m%y') = :dataCriacao");
				
			}
			
			if(nomeParticipante!=null && !nomeParticipante.isEmpty()) {
				parametros.put("nomeParticipante", "%"+nomeParticipante.toUpperCase()+"%");
				jpql.append(" and upper(pa.nome) like :nomeParticipante");
			}
			
			if(cpfCnpjParticipante!=null && !cpfCnpjParticipante.isEmpty()) {
				parametros.put("cpfCnpjParticipante", "%"+cpfCnpjParticipante.toUpperCase()+"%");
				jpql.append(" and upper(pa.cpfCnpj) like :cpfCnpjParticipante or upper(pa.registro) like :cpfCnpjParticipante");
			}
		}
		
		jpql.append(" order by c.casoId");
		
		
		TypedQuery<Object[]> q = entityManager().createQuery(jpql.toString(),Object[].class);
		
		for(Entry<String, Object>entry: parametros.entrySet()){
			if(entry.getKey().equals("dataCriacao")) {				
				q.setParameter(entry.getKey(), (Date)entry.getValue(), TemporalType.DATE);
			}
			else {
				q.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
	    List<Caso> casos = new ArrayList<>();
	    List<Object[]> objects = q.getResultList();
	    Caso casoAtual = null;
	    for(Object[] o  : objects) {
	    	Long casoId = (Long) o[0];
	    	if(casoAtual == null || !casoAtual.getCasoId().equals(casoId)) {
	    		Caso caso = new Caso();
	    		if(caso.getPartes() == null) {
	    			caso.setPartes(new ArrayList<>());
	    		}
		    	caso.setCasoId(casoId);
		    	caso.setValor((BigDecimal) o[1]);
		    	caso.setDescricao((String) o[2]);
		    	caso.setData((Date) o[3]);
		    	caso.setStatus((StatusCaso) o[4]);
		    	casos.add(caso);
		    	casoAtual =  caso;
	    	}
	    	Pessoa pessoa =  new Pessoa();
	    	pessoa.setPessoaId((Long) o[5]);
	    	pessoa.setNome((String) o[6]);
	    	
	    	Parte parte = new Parte();
	    	parte.setTipoParte((TipoParte) o[7]);
	    	parte.setPessoa(pessoa);
	    	parte.setCaso(casoAtual);
	    	casoAtual.getPartes().add(parte);	
	    }
	    return casos;
	}
	
	
	public List<Caso> buscarCasoDoMediador(Long pessoaId){
		
		String query = "select distinct c from Caso c left join fetch c.partes pa"
				+ " join fetch pa.pessoa p0"
				+ " left join fetch c.mediador m "
				+ " left join c.mediadorCaso mc "
				+ " left join mc.mediador m0"
				+ " left join m0.pessoa p0"
				+ " join fetch m.pessoa p1"
				+ " where c.mediador.pessoa.pessoaId =:pId    "
				+ " or ( p0.pessoaId =:pId and mc.statusMediador =:sts )  ";
		TypedQuery<Caso> q = entityManager().createQuery(query,Caso.class) ;
		q.setParameter("pId", pessoaId);
		q.setParameter("sts",StatusMediador.AGUARDANDO_APROVACAO);
		return q.getResultList();	
	}
	
	
	public List<Caso> buscarCasoFiltroMediador(Long idCaso, Enum<StatusCaso> status, Date dataCriacao, String nomeParticipante, String cpfCnpjParticipante, String cpfMediador){
		
		try {
			Map<String,Object>parametros = new HashMap<>();
			
			StringBuilder jpql = new StringBuilder("select c.casoId,c.valor,c.descricao,c.data,c.status,pa.pessoaId,pa.nome,p.tipoParte from Caso c left join c.partes p left join p.pessoa pa left join c.mediador m where c.casoId is not null and m.pessoa.cpfCnpj =:cpfMediador");

			parametros.put("cpfMediador", cpfMediador.toUpperCase());
			
			
			if(idCaso!=null) {
				parametros.put("casoId", idCaso);
				jpql.append(" and c.casoId = :casoId");
			}
			else {
				
				if(status!=null) {
					parametros.put("statusCaso", status);
					jpql.append(" and c.status = :statusCaso");
				}
				
				if(dataCriacao!=null){
					parametros.put("dataCriacao", dataCriacao);
					jpql.append(" and DATE_FORMAT(c.data, '%d%m%y') = :dataCriacao");
					
				}
				
				if(nomeParticipante!=null && !nomeParticipante.isEmpty()) {
					parametros.put("nomeParticipante", "%"+nomeParticipante.toUpperCase()+"%");
					jpql.append(" and upper(pa.nome) like :nomeParticipante");
				}
				
				if(cpfCnpjParticipante!=null && !cpfCnpjParticipante.isEmpty()) {
					parametros.put("cpfCnpjParticipante", "%"+cpfCnpjParticipante.toUpperCase()+"%");
					jpql.append(" and upper(pa.cpfCnpj) like :cpfCnpjParticipante or upper(pa.registro) like :cpfCnpjParticipante");
				}
			}
			
			jpql.append(" order by c.casoId");
			
			TypedQuery<Object[]> q = entityManager().createQuery(jpql.toString(),Object[].class);
			
			for(Entry<String, Object>entry: parametros.entrySet()){
				if(entry.getKey().equals("dataCriacao")) {				
					q.setParameter(entry.getKey(), (Date)entry.getValue(), TemporalType.DATE);
				}
				else {
					q.setParameter(entry.getKey(), entry.getValue());
				}
			}
			
			List<Caso> casos = new ArrayList<>();
			List<Object[]> objects = q.getResultList();
			Caso casoAtual = null;
			for(Object[] o  : objects) {
				Long casoId = (Long) o[0];
				if(casoAtual == null || !casoAtual.getCasoId().equals(casoId)) {
					Caso caso = new Caso();
					if(caso.getPartes() == null) {
						caso.setPartes(new ArrayList<>());
					}
			    	caso.setCasoId(casoId);
			    	caso.setValor((BigDecimal) o[1]);
			    	caso.setDescricao((String) o[2]);
			    	caso.setData((Date) o[3]);
			    	caso.setStatus((StatusCaso) o[4]);
			    	casos.add(caso);
			    	casoAtual =  caso;
				}
				Pessoa pessoa =  new Pessoa();
				pessoa.setPessoaId((Long) o[5]);
				pessoa.setNome((String) o[6]);
				
				Parte parte = new Parte();
				parte.setTipoParte((TipoParte) o[7]);
				parte.setPessoa(pessoa);
				parte.setCaso(casoAtual);
				casoAtual.getPartes().add(parte);	
			}
			return casos;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Caso> buscarCasoDoResponsavel(){
		String jpql = "select c.casoId,c.valor,c.descricao,c.data,c.status,pa.pessoaId,pa.nome,p.tipoParte from Caso c left join c.partes p join p.pessoa pa";
		TypedQuery<Object[]> q = entityManager().createQuery(jpql,Object[].class);
	    List<Caso> casos = new ArrayList<>();
	    List<Object[]> objects = q.getResultList();
	    Caso casoAtual = null;
	    for(Object[] o  : objects) {
	    	Long casoId = (Long) o[0];
	    	if(casoAtual == null || !casoAtual.getCasoId().equals(casoId)) {
	    		Caso caso = new Caso();
	    		if(caso.getPartes() == null) {
	    			caso.setPartes(new ArrayList<>());
	    		}
		    	caso.setCasoId(casoId);
		    	caso.setValor((BigDecimal) o[1]);
		    	caso.setDescricao((String) o[2]);
		    	caso.setData((Date) o[3]);
		    	caso.setStatus((StatusCaso) o[4]);
		    	casos.add(caso);
		    	casoAtual =  caso;
	    	}
	    	Pessoa pessoa =  new Pessoa();
	    	pessoa.setPessoaId((Long) o[5]);
	    	pessoa.setNome((String) o[6]);
	    	
	    	Parte parte = new Parte();
	    	parte.setTipoParte((TipoParte) o[7]);
	    	parte.setPessoa(pessoa);
	    	parte.setCaso(casoAtual);
	    	casoAtual.getPartes().add(parte);	
	    }
	    return casos;
	}
	
	public List<Caso> buscarCasoFiltroAdministrador(Long idCaso, Enum<StatusCaso> status, Date dataCriacao, String nomeParticipante, String cpfCnpjParticipante){
		
		Map<String,Object>parametros = new HashMap<>();
		
		StringBuilder jpql = new StringBuilder("select c.casoId,c.valor,c.descricao,c.data,c.status,pa.pessoaId,pa.nome,p.tipoParte from Caso c left join c.partes  p join p.pessoa pa  where c.casoId is not null");
		
		if(idCaso!=null) {
			parametros.put("casoId", idCaso);
			jpql.append(" and c.casoId = :casoId");
		}
		else {
			
			if(status!=null) {
				parametros.put("statusCaso", status);
				jpql.append(" and c.status = :statusCaso");
			}
			
			if(dataCriacao!=null){
				parametros.put("dataCriacao", dataCriacao);
				jpql.append(" and DATE_FORMAT(c.data, '%d%m%y') = :dataCriacao");
				
			}
			
			if(nomeParticipante!=null && !nomeParticipante.isEmpty()) {
				parametros.put("nomeParticipante", "%"+nomeParticipante.toUpperCase()+"%");
				jpql.append(" and upper(pa.nome) like :nomeParticipante");
			}
			
			if(cpfCnpjParticipante!=null && !cpfCnpjParticipante.isEmpty()) {
				parametros.put("cpfCnpjParticipante", "%"+cpfCnpjParticipante.toUpperCase()+"%");
				jpql.append(" and upper(pa.cpfCnpj) like :cpfCnpjParticipante or upper(pa.registro) like :cpfCnpjParticipante");
			}
		}
		
		jpql.append(" order by c.casoId");
		
		TypedQuery<Object[]> q = entityManager().createQuery(jpql.toString(),Object[].class);
		
		for(Entry<String, Object>entry: parametros.entrySet()){
			if(entry.getKey().equals("dataCriacao")) {				
				q.setParameter(entry.getKey(), (Date)entry.getValue(), TemporalType.DATE);
			}
			else {
				q.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
	    List<Caso> casos = new ArrayList<>();
	    List<Object[]> objects = q.getResultList();
	    Caso casoAtual = null;
	    for(Object[] o  : objects) {
	    	Long casoId = (Long) o[0];
	    	if(casoAtual == null || !casoAtual.getCasoId().equals(casoId)) {
	    		Caso caso = new Caso();
	    		if(caso.getPartes() == null) {
	    			caso.setPartes(new ArrayList<>());
	    		}
		    	caso.setCasoId(casoId);
		    	caso.setValor((BigDecimal) o[1]);
		    	caso.setDescricao((String) o[2]);
		    	caso.setData((Date) o[3]);
		    	caso.setStatus((StatusCaso) o[4]);
		    	casos.add(caso);
		    	casoAtual =  caso;
	    	}
	    	Pessoa pessoa =  new Pessoa();
	    	pessoa.setPessoaId((Long) o[5]);
	    	pessoa.setNome((String) o[6]);
	    	
	    	Parte parte = new Parte();
	    	parte.setTipoParte((TipoParte) o[7]);
	    	parte.setPessoa(pessoa);
	    	parte.setCaso(casoAtual);
	    	casoAtual.getPartes().add(parte);	
	    }
	    return casos;
	}
	
	
	
	public Caso recuperarCaso(Long casoId) {
		TypedQuery<Caso> q = entityManager().createQuery("select c from Caso c join fetch c.partes p join fetch p.pessoa pa left join fetch pa.usuario u left join  c.mediador me where c.casoId =:id ",Caso.class);
		q.setParameter("id",casoId);
		return q.getSingleResult();
	}
	
	
	public Long recuperarQuantidadeCasoPorStatus(Enum<StatusCaso> status) {
		Query q = entityManager().createQuery("select COUNT(c) from Caso c where c.status like :status")
				.setParameter("status", status);
		return (long) q.getSingleResult();
	}
	
	public Long recuperarQuantidadeCaso() {
		Query q = entityManager().createQuery("select COUNT(c) from Caso c");
		return (long) q.getSingleResult();
	}
	
	public Caso recuperarCasoComMediadores(Long casoId) {
		
		TypedQuery<Caso> q = entityManager().createQuery("select c from Caso c left join fetch c.mediadores me where c.casoId =:id ",Caso.class);
		q.setParameter("id",casoId);
		return q.getSingleResult();
	}
	
}
