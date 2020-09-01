package br.com.litero.camara.model;

import java.util.Collections;
import java.util.List;

public class PessoaNotificationHelper {
	
	private List<Pessoa> pessoas;
	
	public PessoaNotificationHelper() {
	}
	
	public PessoaNotificationHelper adicionarPessoa(Pessoa pessoa) {
		if(pessoa != null) {
		this.pessoas.add(pessoa);
		}
		return this;
	}
	
	public PessoaNotificationHelper adicionarPessoas(List<Pessoa> pessoas) {
		if(pessoas != null && !pessoas.isEmpty()) {
			pessoas.forEach(p ->{
				this.pessoas.add(p);
			});
		}
		return this;
	}
	
	public List<Pessoa> build(){
		return Collections.unmodifiableList(this.pessoas);
	}

}
