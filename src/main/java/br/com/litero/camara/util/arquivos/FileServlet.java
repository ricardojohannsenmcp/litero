package br.com.litero.camara.util.arquivos;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/file/*")
public class FileServlet extends HttpServlet{

	
	private static final long serialVersionUID = 1L;
	
	
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		Path root = FileSystems.getDefault().getPath(System.getenv("HOME"),"Anexos");
		String serverPath = root.toString()+java.io.File.separator;

		
		String path = request.getRequestURI().split("/file")[1];
		
		
		
		Path source = Paths.get(serverPath + path);
		
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentType = fileNameMap.getContentTypeFor("file:"+source);
		
		response.setContentType(contentType);
		response.setHeader("Content-Length", String.valueOf(Files.size(source)));
		response.setHeader("Content-Disposition", "fileName=\""+source.getFileName().toString()+"\"");
		
		FileSaver.transfer(source,response.getOutputStream());

		
	}

}
