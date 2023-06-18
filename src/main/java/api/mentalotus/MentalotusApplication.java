package api.mentalotus;

import api.mentalotus.Network.Listener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MentalotusApplication {

	public static void main(String[] args) {
		//Listener listener;
		//listener = new Listener(7777, Listener.Session.CLOVA);
		//listener = new Listener(7777, Listener.Session.CHATGPT);



		SpringApplication.run(MentalotusApplication.class, args);
	}

}

@Configuration
class AppConfig {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}