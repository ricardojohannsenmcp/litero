package br.com.litero.camara.util.arquivos;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;


@ApplicationScoped
public class FileSaver {
	

	
	public String write(Part arquivo) {
		
		try {
			
			Path root = FileSystems.getDefault().getPath(System.getenv("HOME"),"Anexos");	
			
			Files.createDirectories(root);
			
			
			String serverPath = root.toString()+java.io.File.separator;
			
			String finalFileName = encodeFileName(arquivo.getSubmittedFileName());
			
			String relativePath = finalFileName ;
			
			
			arquivo.write(serverPath + relativePath);
			return finalFileName;
		} catch (IOException e) {
			
			throw new RuntimeException(e);
		}
		
		
	}
	
	
	
	
	
	public String write(String fileName,byte[] bytes)  {
		
		try {
			
			
			System.out.println("len->"+bytes.length);
			
			Path root = FileSystems.getDefault().getPath(System.getenv("HOME"),"Anexos");	
			
			Files.createDirectories(root);
			
			
			String serverPath = root.toString()+java.io.File.separator;
			
			String finalFileName = encodeFileName(fileName);
			
			String relativePath = finalFileName ;
			
			
			//uploadedFile.write(serverPath + relativePath);
			
			
			
			IOUtils.copy(new ByteArrayInputStream(bytes), new FileOutputStream(serverPath + relativePath));
			
			
			return finalFileName;
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		} 
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

	public static void transfer(Path source, OutputStream outputStream) {

		try {
			FileInputStream in = new FileInputStream(source.toFile());
			
			
			try(ReadableByteChannel inputChanel = Channels.newChannel(in);  WritableByteChannel outputChanel=   Channels.newChannel(outputStream)){
				
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);//10kb
				
				while(inputChanel.read(buffer) != -1) {
					buffer.flip();
					
					outputChanel.write(buffer);
					
					buffer.clear();
				}
				
			} catch (IOException e) {
				throw new RuntimeException(e);

			};
			
			
		} catch (FileNotFoundException e) {
			
			throw new RuntimeException(e);
		}
		
	}
	
	private String encodeFileName(String fileName) {
		
		String extensao  = fileName.substring(fileName.length()-4,fileName.length());
		
		String newFileName = String.valueOf(new Date().getTime());

		
		return newFileName + extensao;
	}
	
	
	

	
	
}
