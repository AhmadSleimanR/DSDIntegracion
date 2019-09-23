package pe.edu.upc.integracion.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import pe.edu.upc.integracion.model.Aprender;
import pe.edu.upc.integracion.model.Country;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import pe.edu.upc.integracion.service.CloudAMQPService;

@Repository
public class AprenderRepository {
	@Autowired
	RestTemplate restTemplate;
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	public boolean aprender(Aprender aprender){
		Country[] country = restTemplate.getForObject( "https://restcountries.eu/rest/v2/capital/"+aprender.getCapital(), Country[].class);
		Country country1 = Arrays.asList(country).get(0);
		if(country1.getName().equals(aprender.getPais())){
			return true;
		}
		else{
//			mandarSMSService(aprender.getNombre()+", la operación fue incorrecta, el país de donde "+aprender.getCapital()
//					+" es capital, se llama "+country1.getName(), aprender.getCelular());
			//Se fija el tiempo máximo de espera para conectar con el servidor (5000)
//Se fija el tiempo máximo de espera de la respuesta del servidor (60000)
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(5000)
					.setSocketTimeout(60000)
					.build();

			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setDefaultRequestConfig(config);
			CloseableHttpClient httpClient = builder.build();
			HttpPost post = new HttpPost("http://www.altiria.net/api/http");
			List parametersList = new ArrayList();
			parametersList.add(new BasicNameValuePair("cmd", "sendsms"));
			parametersList.add(new BasicNameValuePair("domainId", "test"));
			parametersList.add(new BasicNameValuePair("login", "TU_USUARIO_DE_LOGIN_AQUI"));
			parametersList.add(new BasicNameValuePair("passwd", "TU_PASSWORD_AQUI"));
			parametersList.add(new BasicNameValuePair("dest", "51"+aprender.getCelular()));
			parametersList.add(new BasicNameValuePair("dest", "51"+aprender.getCelular()));
			parametersList.add(new BasicNameValuePair("msg", aprender.getNombre()+", la operación fue incorrecta, el país de donde "+aprender.getCapital()
			+" es capital, se llama "+country1.getName()));
			try {
				//Se fija la codificacion de caracteres de la peticion POST
				post.setEntity(new UrlEncodedFormEntity(parametersList,"UTF-8"));
			}
			catch(UnsupportedEncodingException uex) {
				System.out.println("ERROR: codificación de caracteres no soportada");
			}

			CloseableHttpResponse response = null;

			try {
				System.out.println("Enviando petición");
				//Se envía la petición
				response = httpClient.execute(post);
				//Se consigue la respuesta
				String resp = EntityUtils.toString(response.getEntity());

				//Error en la respuesta del servidor
				if (response.getStatusLine().getStatusCode()!=200){
					System.out.println("ERROR: Código de error HTTP:  " + response.getStatusLine().getStatusCode());
					System.out.println("Compruebe que ha configurado correctamente la direccion/url ");
					System.out.println("suministrada por Altiria");
				}else {
					//Se procesa la respuesta capturada en la cadena 'response'
					if (resp.startsWith("ERROR")){
						System.out.println(resp);
						System.out.println("Código de error de Altiria. Compruebe las especificaciones");
					} else
						System.out.println(resp);
				}
			}
			catch (Exception e) {
				System.out.println("Excepción");
				e.printStackTrace();
				return false;
			}
			finally {
				//En cualquier caso se cierra la conexión
				post.releaseConnection();
				if(response!=null) {
					try {
						response.close();
					}
					catch(IOException ioe) {
						System.out.println("ERROR cerrando recursos");
					}
				}
			}
			CloudAMQPService cloudAMQPService = new CloudAMQPService();
			try{
			cloudAMQPService.sendMessage(aprender.getNombre()+aprender.getCelular()+aprender.getCapital()+aprender.getPais()
					+" SMS: "+aprender.getNombre()+", la operación fue incorrecta, el país de donde "+aprender.getCapital()
					+" es capital, se llama "+country1.getName());
			}catch (Exception e){
			}
			return false;
		}
	}

//	private void mandarSMSService(String mensaje,Integer celular){
////		try {
////			String passwordHash = "354fad95a31ec1839be596ccbb55c2d1";
////			BasicAuthClient client = new BasicAuthClient("werty_51@hotmail.com", passwordHash);
////
////			SmsFactory smsApi = new SmsFactory(client, new ProxyNative("https://api.smsapi.com/"));
////			String phoneNumber = "51"+celular;
////			SMSSend action = smsApi.actionSend()
////					.setText("test")
////					.setTo(phoneNumber);
////
////			StatusResponse result = action.execute();
////
////			for (MessageResponse status : result.getList() ) {
////				System.out.println(status.getNumber() + " " + status.getStatus());
////			}
////		} catch (ClientException e) {
////			e.printStackTrace();
////		} catch (SmsapiException e) {
////			e.printStackTrace();
////		}
////	}
}
