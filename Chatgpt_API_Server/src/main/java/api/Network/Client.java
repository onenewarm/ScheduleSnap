package api.Network;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public Session Connect(String ip, int port) {
        OutputStream out = null;
        InputStream in = null;

        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(ip, port);
            out = socket.getOutputStream();
            in = socket.getInputStream();


            String outputMessage;
            if (port == 7777) {
                MainSession session = new MainSession(socket, out, in);
                SessionManager.GMainServerSession = session;
                outputMessage = "CHATGPT";
                System.out.println("TEST Client 연결 성공");
                byte[] data = outputMessage.getBytes();
                out.write(data);
                out.flush();
                return session;
            } else if (port == 8888) {
                ClovaSession session = new ClovaSession(socket, out, in);
                SessionManager.GClovaServerSession = session;
                outputMessage = "CHATGPT";
                System.out.println("TEST Client 연결 성공");
                byte[] data = outputMessage.getBytes();
                out.write(data);
                out.flush();
                return session;
            } else {
                outputMessage = "ERROR";
                byte[] data = outputMessage.getBytes();
                out.write(data);
                out.flush();
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            try{
                Thread.sleep(3000);
            } catch(Exception sleepE){

            }

            Session session = Connect(ip, port);
            return session;
        }
    }
}

