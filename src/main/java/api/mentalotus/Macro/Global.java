package api.mentalotus.Macro;


import api.mentalotus.Domain.UploadImg;
import org.springframework.web.multipart.MultipartFile;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Global {
    public static HashMap<String, MultipartFile> Images = new HashMap<>();
    private static HashMap<String, UploadImg> Statuses = new HashMap<>();
    private static ReentrantLock _lock = new ReentrantLock();


    public static Socket ClovaSocket;
    public static Socket ChatGPTSocket;

    public static void SetStatus(String scheduleKey, int status)
    {
        _lock.lock();
        Statuses.get(scheduleKey).set_status(status);
        _lock.unlock();
    }

    public static void AddStatus(String scheduleKey, UploadImg status)
    {
        Statuses.put(scheduleKey, status);
    }
}
