package br.com.litero.camara.model;

public enum TipoCobranca {
	
	
	TAXA_DE_CADASTRO("Taxa de Cadastro");
	
	
	private TipoCobranca(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	

}
