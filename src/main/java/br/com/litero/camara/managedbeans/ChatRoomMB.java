package br.com.litero.camara.managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatRoomMB {
	
	
	
	private List<Long> rooms = new ArrayList<>();
	
	
	
	
	public void on(Long room) {
		
		rooms.add(room);
	}
	
	public void off(Long room) {
		
		rooms.remove(room);
		room = null;
	}
	
	public boolean isOnline(Long room) {
		if(room == null) {
			
			throw new RuntimeException("A sala está nula");
		}
		
		return rooms.contains(room);
	}
	
	

}
