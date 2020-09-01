package br.com.litero.camara.model;

public enum EstadoCivil {
	
	
	
	
	
	SOLTEIRO("Solteiro(a)"),CASADO("Casado(a)"),DIVORCIADO("Divorciado(a)"),UNIAO_ESTAVEL("União Estável"),VIUVO("Viuvo(a)");
	
	
	
	
	private EstadoCivil(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}
	
	
	
	

}
