package br.com.litero.camara.model;

public enum TipoEvento {
	
	PROTOCOLO("Protocolo","glyphicon glyphicon-check","warning"),
	CASO_NEGADO("Caso negado","glyphicon glyphicon-ban-circle","danger"),
	CASO_ACEITO("Caso aceito","glyphicon glyphicon-check","success"),
	CASO_ENCERRADO("Caso encerrado","glyphicon glyphicon-check","danger"),
	PROCESSO_COMUNICACAO("Processo de Comunição com o requerido","glyphicon glyphicon-phone","default"),
	CONVITE_MEDIADOR("Foi enviado um convite para o mediador","","default"),
	RESPOSTA_CONVITE("Resposta ao convite de participação","","default"),
	INCLUSAO_ESPECIALISTA("Inclusão de especialista pelo requerente","glyphicon glyphicon-user","primary"),
	RESPOSTA_MEDIADOR("Resposta do mediador convidado","glyphicon glyphicon-user","primary"),
	APROVACAO_MEDIADOR("Aprovação do mediador pelo requerido","glyphicon glyphicon-user","warning"),
	REPROVACAO_MEDIADOR("Reprovação do mediador pelo requerido","glyphicon glyphicon-user","danger"),
	ARQUIVO_ANEXADO("Arquivo anexado","glyphicon glyphicon-paperclip","success"),
	INCLUSAO_PROPOSTA("Inclusão de proposta","glyphicon glyphicon-usd","primary"),
	RESPOSTA_PROPOSTA("Respondeu à proposta","glyphicon glyphicon-comment","primary");
	
	
	
	private String descricao;
	
	private String icon;
	
	private String color;
	

	


	private TipoEvento(String descricao, String icon, String color) {
		this.descricao = descricao;
		this.icon = icon;
		this.color = color;
	}



	public String getDescricao() {
		return descricao;
	}



	public String getIcon() {
		return icon;
	}



	public String getColor() {
		return color;
	}
	
	
	
	
	

}
