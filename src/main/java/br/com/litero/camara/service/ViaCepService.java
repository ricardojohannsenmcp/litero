package br.com.litero.camara.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.enterprise.context.RequestScoped;

import org.primefaces.json.JSONObject;

import br.com.litero.camara.model.Endereco;

@RequestScoped
public class ViaCepService implements CepService{
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	
	

	
	public Endereco buscarCep(String cepEntrada) {
		
		Endereco endereco = null;
		try {
			String url = "http://viacep.com.br/ws/"+cepEntrada+"/json/";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

	
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			//print result
			String resposta = response.toString();
			
			 endereco = new Endereco();
			
			JSONObject jsonObject = new JSONObject(resposta);
		 endereco.setLogradouro((String) jsonObject.get("logradouro"));	
		 endereco.setCidade((String) jsonObject.get("localidade"));
		 endereco.setUf((String) jsonObject.get("uf"));
		 endereco.setBairro((String) jsonObject.get("bairro"));
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return endereco;

	}

}
