package br.com.litero.camara.util.json;

public enum JsonMessageType {
	
	
	MENSSAGEM("menssagem"),ANEXO("anexo"),PROPOSTA("proposta"),DOCUMENTO("documento"),CASO("caso");
	
	
	private String descricao;
	
	
	

	private JsonMessageType(String descricao) {
		this.descricao = descricao;
	}




	public String getDescricao() {
		return descricao;
	}
	
	
	

}
