package br.com.litero.camara.repositorios;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Area;

@Repository
public  abstract class AreaRepository extends AbstractEntityRepository<Area,Long> {

}
