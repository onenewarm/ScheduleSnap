package api.Service;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ServiceManager {

    public static clovaService GClovaService = new clovaService();
    public static ChatGptService GChatGptService = new ChatGptService();
}