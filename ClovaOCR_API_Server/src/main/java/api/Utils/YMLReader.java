package api.Utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class YMLReader {
    private ClovaConfig clovaConfig;
    private ServerConfig serverConfig;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ClovaConfig
    {
        private String KEY;
        private String URL;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ServerConfig
    {
        private String MAINSERVERIP;
        private int MAINSERVERPORT;
    }



}


