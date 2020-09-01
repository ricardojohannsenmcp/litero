package br.com.litero.camara.repositorios;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.MediadorCaso;

@Repository
public abstract class MediadorCasoRepository extends AbstractEntityRepository<MediadorCaso, Long> {

}
