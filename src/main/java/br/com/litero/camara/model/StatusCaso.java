package br.com.litero.camara.model;

public enum StatusCaso {
	
	EM_ANALISE("Em análise", "primary"),
	CASO_ACEITO("Caso Aceito - Aguardando Pagamento", "success"),
	PROCESSO_COMUNICACAO("Processo de comunicação","info"),
	TRAMITANDO("Tramitando", "primary"),
	AGUARDANDO_DEFINICAO_MEDIADOR("Aguardando Mediador", "warning"),
	AGUARDANDO_DEFINICAO_MEDIADOR_CAMARA("Aguardando Mediador pela Câmara", "warning"),
	AGUARDANDO_VALIDACAO_MEDIADOR("Aguardando Validação do Mediador", "warning"),
	AGUARDANDO_ACEITE_CONVIDADO("Aguardando Aceite pelo Convidado", "warning"),
	NEGADO("Negado","danger"),
	IMPASSE("Impasse", "danger"),
	ENCERRADO("Encerrado", "warning");
	
	private String descricao;
	
	private String severity;
	
	

	private StatusCaso(String descricao, String severity) {
		this.descricao = descricao;
		this.severity = severity;
	}



	public String getDescricao() {
		return descricao;
	}



	public String getSeverity() {
		return severity;
	}
	
	
	
	
	

}
