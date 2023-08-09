package api.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatGPTResponse {
    private List<ChatGptChoice> choices;
}