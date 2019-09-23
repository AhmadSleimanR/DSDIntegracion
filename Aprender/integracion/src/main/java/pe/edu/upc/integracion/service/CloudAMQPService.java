package pe.edu.upc.integracion.service;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloudAMQPService {
	private final static String QUEUE_NAME = "TU_COLA_AQUI";

	public void sendMessage(String message) throws Exception {
		String uri = System.getenv("CLOUDAMQP_URL");
		if (uri == null) uri = "TU_URL_COMPLETA_AQUI";
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(uri);
		factory.setConnectionTimeout(30000);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
	}
}