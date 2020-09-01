package br.com.litero.camara.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.TipoUsuario;
import br.com.litero.camara.model.Usuario;
import br.com.litero.camara.model.UsuarioForm;
import br.com.litero.camara.repositorios.PessoaRepository;
import br.com.litero.camara.repositorios.UsuarioRepository;

@ApplicationScoped
public class PessoaService {
	
	@Inject
	private PessoaRepository pessoaRepository;
	@Inject
	private UsuarioRepository usuarioRepository;
	
	
	public List<Pessoa> buscarPessoas(String filtro){
		return pessoaRepository.buscarPorNomeCpfCnpj(filtro);
	}
	
	public Pessoa buscarPorId(Long id) {
		return pessoaRepository.findBy(id);
	}
	
	public List<Pessoa> recuperarTodos(){
		return pessoaRepository.findAll();
	}
	
	
	@Transactional
	public Pessoa adicionar(Pessoa pessoa) {
		
		return pessoaRepository.save(pessoa);
	}
	
	
	@Transactional
	public void remover(Pessoa pessoa) {
		pessoaRepository.remove(pessoaRepository.save(pessoa));
	}
	
	public Pessoa buscarPorCpfCnpjIgual(String cpfCnpj) {
		return pessoaRepository.buscarPorNomeCpfCnpjIgual(cpfCnpj);
	}
	
	
	@Transactional
	public Pessoa adicionarOuAtualizar(Pessoa pessoa) {
			pessoaRepository.save(pessoa);
		return pessoa;
	}
	
	
	@Transactional
	public Pessoa adicionar(Pessoa pessoa,UsuarioForm usuarioForm) {
		pessoa = pessoaRepository.save(pessoa);
				Usuario usuario = new Usuario();
				usuario.setLogin(usuarioForm.getLogin());
				usuario.setSenha(usuarioForm.getSenha());
				usuario.setPessoa(pessoa);
				usuario.setTipoUsuario(TipoUsuario.PARTE);
				pessoa.setUsuario(usuario);
				usuarioRepository.save(usuario);
		return pessoa;
	}
	
	

}
