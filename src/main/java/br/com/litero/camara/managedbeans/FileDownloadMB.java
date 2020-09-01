package br.com.litero.camara.managedbeans;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@RequestScoped
public class FileDownloadMB {
	
	
	 
	 
	 
	public StreamedContent download(String caminho) {
		
		try {
			Path root = FileSystems.getDefault().getPath(System.getenv("HOME"),"Anexos");	
			
			StreamedContent file = new DefaultStreamedContent(new FileInputStream(root.toString()+ "/"+caminho),null,caminho);
			
			return file;
		} catch (FileNotFoundException e) {
			
			throw new RuntimeException("erro ao realizar download do arquivo");
		}
	}

	
	
	 
	 
	 
	 
	 

}
