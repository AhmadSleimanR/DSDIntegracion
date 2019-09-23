package pe.edu.upc.integracion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import pe.edu.upc.integracion.model.Aprender;
import pe.edu.upc.integracion.service.AprenderService;

@Slf4j
@SpringBootApplication(exclude= RabbitAutoConfiguration.class)
public class IntegracionApplication implements CommandLineRunner {

	@Autowired
	AprenderService aprenderService;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(IntegracionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Aprender aprender = new Aprender();
		aprender.setCapital("Lima");
		aprender.setNombre("Ahmad Sleiman Romero");
		aprender.setCelular(999558838);
		aprender.setPais("Peru");
		if(aprenderService.aprender(aprender))
			log.info("correct");
		else
			log.info("incorrect");
	}
}
