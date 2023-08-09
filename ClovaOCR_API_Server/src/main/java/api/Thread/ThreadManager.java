package api.Thread;

import java.util.ArrayList;

public class ThreadManager {

    public static ThreadManager GThreadManager;
    public ArrayList<MyThread> threadArr;

    public void runThread(MyThread thread)
    {
        threadArr.add(thread);
        thread.run();
    }
}
