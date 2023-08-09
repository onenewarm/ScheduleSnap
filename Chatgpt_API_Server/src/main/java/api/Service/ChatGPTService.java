package api.Service;

import api.Main.Global;
import api.model.ChatGPTRequest;
import api.model.ChatGPTResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@NoArgsConstructor
public class ChatGPTService {

    private static RestTemplate restTemplate= new RestTemplate();

    public String ask(String ocrResult) {
        String query = Global.YMLReader.getChatGPTConfig().getBASE_QUERY() + ocrResult;
        ChatGPTRequest request = new ChatGPTRequest(Global.YMLReader.getChatGPTConfig().getMODEL(), query);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ Global.YMLReader.getChatGPTConfig().getKEY());
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(Global.YMLReader.getChatGPTConfig().getURL(),
                new HttpEntity<>(request, headers),
                ChatGPTResponse.class);

        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
}
