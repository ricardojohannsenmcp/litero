package br.com.litero.camara.security;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns="/*")
public class SecurityFilter implements Filter {
	
	
	@Inject
	private UsuarioWeb usuarioWeb;
	
	
	
	private static final String LOGIN_PAGE = "/Login.xhtml";
	private static final String FACES_RESOURCES = "/javax.faces.resource";
	private static final String CADASTRO_PAGE = "/Cadastro.xhtml";
	private static final String ERROR_PAGE = "/Erro.xhtml";
	private static final String CONFIRM_PAGE = "/ConfirmacaoCadastro.xhtml";
	
	@Override
	public void init(FilterConfig config) throws ServletException {}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        if (isUrlPermitida(request) || isUsuarioLogado()) {
        		chain.doFilter(req, res);
        } else {
        		response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
        }
	}
	
	private boolean isUsuarioLogado() {
		
		
		
		return usuarioWeb != null
        			&& usuarioWeb.isLogado();
	}

	private boolean isUrlPermitida(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.equals(LOGIN_PAGE) || path.equals(CADASTRO_PAGE) || path.equals(ERROR_PAGE) || path.equals(CONFIRM_PAGE)
				|| path.startsWith(FACES_RESOURCES);
	}

	@Override
	public void destroy() {}

}
