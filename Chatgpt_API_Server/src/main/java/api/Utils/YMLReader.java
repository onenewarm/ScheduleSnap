package api.Utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class YMLReader {
    private ChatGPTConfig chatGPTConfig;
    private ServerConfig serverConfig;


    @Getter
    @Setter
    @NoArgsConstructor
    public static class ChatGPTConfig
    {
        private String URL;
        private String KEY;
        private String MODEL;
        private String BASE_QUERY;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ServerConfig
    {
        private String MAINSERVERIP;
        private String CLOVASERVERIP;
        private int MAINSERVERPORT;
        private int CLOVASERVERPORT;
    }
}


