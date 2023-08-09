package api.mentalotus.Main;


import SocketSharedData.GptResult;
import api.mentalotus.Domain.UploadImg;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;


@Getter
@Setter
public class Global {
    private static HashMap<String, String> ChatGptResults = new HashMap<>();
    private static ReentrantLock _imglock = new ReentrantLock();
    public static Queue<UploadImg> imgQueue = new LinkedList<>();
    public static HashMap<String,String> GGptResultMap = new HashMap<String,String>();
}
