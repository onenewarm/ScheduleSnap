package api.Main;

import SocketSharedData.GptResult;
import api.Utils.YMLReader;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class Global {
    public static volatile Queue<GptResult> CHATGPTResults = new LinkedList<>();
    public static Object CHATGPTLock = new Object();
    public static YMLReader YMLReader;
}
