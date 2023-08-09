package api.mentalotus;

import api.mentalotus.Network.Listener;
import api.mentalotus.Network.SessionManager;
import api.mentalotus.Thread.MyThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MainServer {

	public static void main(String[] args) {
		Listener listener = new Listener(7777);

		for(int i=0;i<2;++i) // 테스트에서는 1번이지만, 실제로는 2번 필요. Clova OCR Server / Chat GPT Server 각각의 Connect가 필요
		{
			listener.Connect();
		}

		{
			Runnable targetRunnable = new Runnable() {
				@Override
				public void run() {
					SessionManager.GChatGptSession.ProcessRecv();
				}
			};

			MyThread myThread = new MyThread(targetRunnable);
			Thread thread = new Thread(myThread);
			thread.start();
		}

		SpringApplication.run(MainServer.class, args);
	}

}

@Configuration
class AppConfig {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}