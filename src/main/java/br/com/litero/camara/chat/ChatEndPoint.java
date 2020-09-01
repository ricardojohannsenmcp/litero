package br.com.litero.camara.chat;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import br.com.litero.camara.util.json.JsonObjectMessage;

@ServerEndpoint("/chat/{room}")
public class ChatEndPoint implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	private static List<Session> sessions =  new ArrayList<>();

	
	
	
	
	@OnOpen
	public void onOpen(Session session, @PathParam("room") final String room, EndpointConfig endpointConfig) {
		session.getUserProperties().put("room", room);
		sessions.add(session);
	}





	public void send(JsonObjectMessage mensagem) {
			
			
		
		for(Session session : sessions) {	
			String room = (String) session.getUserProperties().get("room");
			if(session.isOpen() && room.equals(mensagem.getCasoId()) ) {
				try {
					session.getBasicRemote().sendText(mensagem.getJsonObject().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
	
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) throws IOException {
		
		try{
			sessions.remove(session);
		}finally {
			 session.close();
		}
	}
	
	
	
	
	



}
