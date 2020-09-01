package br.com.litero.camara.util.json;

import org.primefaces.json.JSONObject;

public class JsonObjectMessage {
	
	

	private String casoId;
	private JSONObject jsonObject;
	

	public JsonObjectMessage(String casoId, JSONObject jsonObject) {
		this.casoId = casoId;
		this.jsonObject = jsonObject;
	}
	
	
	public String getCasoId() {
		return casoId;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	
	
	
	
	
	

}
