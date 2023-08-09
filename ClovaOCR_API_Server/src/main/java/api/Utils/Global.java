package api.Utils;

import SocketSharedData.OcrResult;

import java.util.LinkedList;
import java.util.Queue;

public class Global {
    public static Queue<OcrResult> OcrResults = new LinkedList<>();
    public static Object ocrLock = new Object();
    public static YMLReader YMLReader;


}
