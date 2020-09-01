package br.com.litero.camara.model;

public enum MotivoFinalizacao {
	
	
	
	DIREITO_INDISPONIVEL("Direito Indisponível"),
	INSTITUICAO_INCOMETENTE("Instituição Incometente"),
	TERMO_DE_ACORDO("Termo de Acordo");
	
	
	
	
	private MotivoFinalizacao(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}
	
	
	
	
	
	

}
