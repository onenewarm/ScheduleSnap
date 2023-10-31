package api.Main;
import api.Network.Client;
import api.Network.ClovaSession;
import api.Network.MainSession;
import api.Network.SessionManager;

import api.Thread.MyThread;
import api.Utils.YMLReader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;


@SpringBootApplication
public class ChatGPTServer {
    public static void main(String[] args) {

        try {
            Yaml yaml = new Yaml(new Constructor(YMLReader.class));
            Global.YMLReader = (YMLReader) yaml.load(new ClassPathResource("application.yml").getInputStream());
        } catch (Exception e) {
            System.out.println(e.getCause());
        }

        Client client = new Client();

        SessionManager.GMainServerSession = (MainSession) client.Connect(Global.YMLReader.getServerConfig().getMAINSERVERIP(),
                Global.YMLReader.getServerConfig().getMAINSERVERPORT());
        SessionManager.GClovaServerSession = (ClovaSession) client.Connect(Global.YMLReader.getServerConfig().getCLOVASERVERIP(),
                Global.YMLReader.getServerConfig().getCLOVASERVERPORT());


        for(int i=0;i<1;++i)
        {
            {
                Runnable targetRunnable = new Runnable() {
                    @Override
                    public void run() {
                        SessionManager.GClovaServerSession.ProcessRecv();
                    }
                };

                MyThread myThread = new MyThread(targetRunnable);
                Thread thread = new Thread(myThread);
                thread.start();
            }
        }

        for(int i=0;i<1;++i)
        {
            {
                Runnable targetRunnable = new Runnable() {
                    @Override
                    public void run() {
                        SessionManager.GMainServerSession.ProcessRecv();
                    }
                };

                MyThread myThread = new MyThread(targetRunnable);
                Thread thread = new Thread(myThread);
                thread.start();
            }
        }

        {
            Runnable targetRunnable = new Runnable() {
                @Override
                public void run() {
                    SessionManager.GMainServerSession.ProcessSend();
                }
            };

            MyThread myThread = new MyThread(targetRunnable);
            Thread thread = new Thread(myThread);
            thread.start();
        }

        // SpringApplication.run(ChatGPTServiceTest.class, args);
    }
}


