package api.Service;

import api.Main.Global;
import api.model.ChatGPTRequest;
import api.model.ChatGPTResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Getter
@Setter
@NoArgsConstructor
public class ChatGPTService {

    private static AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

    public CompletableFuture<String> ask(String ocrResult) {
        String query = Global.YMLReader.getChatGPTConfig().getBASE_QUERY() + ocrResult;
        ChatGPTRequest request = new ChatGPTRequest(Global.YMLReader.getChatGPTConfig().getMODEL(), query);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + Global.YMLReader.getChatGPTConfig().getKEY());

        HttpEntity<ChatGPTRequest> entity = new HttpEntity<>(request, headers);

        ListenableFuture<ResponseEntity<ChatGPTResponse>> futureResponse = asyncRestTemplate.exchange(
                Global.YMLReader.getChatGPTConfig().getURL(),
                HttpMethod.POST,
                entity,
                ChatGPTResponse.class
        );

        // CompletableFuture를 사용하여 비동기 응답을 반환합니다.
        CompletableFuture<String> result = new CompletableFuture<>();

        futureResponse.addCallback(
                response -> {
                    ChatGPTResponse chatGPTResponse = response.getBody();
                    String content = chatGPTResponse.getChoices().get(0).getMessage().getContent();
                    result.complete(content); // 비동기 결과를 완료합니다.
                },
                ex -> {
                    result.completeExceptionally(ex); // 에러 처리
                }
        );

        return result;
    }
}
