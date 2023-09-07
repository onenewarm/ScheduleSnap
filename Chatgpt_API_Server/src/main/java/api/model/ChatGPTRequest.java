package api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatGPTRequest {

    private String model;
    private List<Message> messages;
    private double temperature;

    public ChatGPTRequest(String model, String query) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", query));
        this.temperature = 0;
    }
}