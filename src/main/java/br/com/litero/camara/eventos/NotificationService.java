package br.com.litero.camara.eventos;

import javax.enterprise.event.Observes;

import br.com.litero.camara.model.Evento;

public class NotificationService {
	
	//TODO esta classe vai ser o ponto de partida para o envio de emails
	
	public void enviarNotificacao(@Observes Evento evento) {
		
		
		
		
		//String hql =" select p.nome,p.pessoaId from Caso c "
		
		System.out.println("======= disparando evento =======");
	}

}
