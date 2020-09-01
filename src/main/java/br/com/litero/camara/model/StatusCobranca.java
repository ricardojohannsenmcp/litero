package br.com.litero.camara.model;

public enum StatusCobranca {
	
	
	EM_ABERTO("Em aberto"),PAGO("Pago");
	
	private String descricao;
	
	
	
	

	private StatusCobranca(String descricao) {
		this.descricao = descricao;
	}





	public String getDescricao() {
		return descricao;
	}
	
	
	

}
