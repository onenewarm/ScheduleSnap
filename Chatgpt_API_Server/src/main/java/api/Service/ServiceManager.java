package api.Service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceManager {
    public static ChatGPTService GChatGPTService = new ChatGPTService();
}
