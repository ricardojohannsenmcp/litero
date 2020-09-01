package br.com.litero.camara.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

@ApplicationScoped
public class MailSender {
	
/*	public void enviar(String from,String to,String subject,String htmlEmailTemplate){
		try {
			URL url = new URL("http://www.apache.org");
			  ImageHtmlEmail email = new ImageHtmlEmail();
			  email.setDataSourceResolver(new DataSourceUrlResolver(url));
			  email.setHostName("mail.rjsisweb.com.br");
			  email.setSmtpPort(587);
			  email.setAuthenticator(new DefaultAuthenticator(from, "Rjpe#reir@10"));
			  email.addTo(to);
			  email.setFrom(from);
			  email.setSubject(subject);
			  email.setHtmlMsg(htmlEmailTemplate);
			  email.setTextMsg("Your email client does not support HTML messages");
			  email.send();
		} catch (MalformedURLException | EmailException e) {
			throw new RuntimeException(e);
		}
	}*/
	
	
	public void enviar(String from,String to,String subject,String htmlEmailTemplate){
		try {
			URL url = new URL("http://www.apache.org");
			  ImageHtmlEmail email = new ImageHtmlEmail();
			  email.setDataSourceResolver(new DataSourceUrlResolver(url));
			  email.setHostName("mail.rjsisweb.com.br");
			  email.setSmtpPort(587);
			  email.setAuthenticator(new DefaultAuthenticator(from, "Rjpe#reir@10"));
			  email.addTo(to);
			  email.setFrom(from);
			  email.setSubject(subject);
			  email.setHtmlMsg(htmlEmailTemplate);
			  email.setTextMsg("Your email client does not support HTML messages");
			  email.send();
		} catch (MalformedURLException | EmailException e) {
			throw new RuntimeException(e);
		}
	}
	

}
