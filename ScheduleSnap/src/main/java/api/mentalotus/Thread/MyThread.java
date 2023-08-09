package api.mentalotus.Thread;

public class MyThread implements Runnable{
    private Runnable target;

    public MyThread(Runnable target) {
        this.target = target;
    }

    @Override
    public void run()
    {
        target.run();
    }
}
