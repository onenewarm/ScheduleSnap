package api;

import api.Network.Client;
import api.Network.Server;
import api.Thread.MyThread;
import api.Utils.Global;
import api.Utils.YMLReader;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@SpringBootApplication
public class ClovaServer {

    public static void main(String[] args) {

        try{
            Yaml yaml = new Yaml(new Constructor(YMLReader.class));
            Global.YMLReader = (YMLReader) yaml.load(new ClassPathResource("application.yml").getInputStream());
        }catch(Exception e)
        {
            System.out.println(e.getCause());
        }

        Client client = new Client();
        client.Connect();

        Server server = new Server();
        server.Listen();
        {
            Runnable targetRunnable = new Runnable() {
                @Override
                public void run() {
                    Client.ProcessClient();
                }
            };

            MyThread myThread = new MyThread(targetRunnable);
            Thread thread = new Thread(myThread);
            thread.start();
        }

        {
            Runnable targetRunnable = new Runnable() {
                @Override
                public void run() {
                    Server.ProcessServer();
                }
            };

            MyThread myThread = new MyThread(targetRunnable);
            Thread thread = new Thread(myThread);
            thread.start();
        }


    }

}
