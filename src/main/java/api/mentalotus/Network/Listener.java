package api.mentalotus.Network;
import api.mentalotus.Macro.Global;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Listener {

    public enum Session
    {
        CHATGPT,
        CLOVA
    }
    private ServerSocket serverSocket;
    private Socket socket;

    public Listener(int port, Session session)
    {
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("[Server실행] Client연결대기중...");
            socket = serverSocket.accept();			// 연결대기

            System.out.println("Client 연결됨.");

            switch (session){
                case CLOVA:
                    Global.ClovaSocket = socket;
                    break;
                case CHATGPT:
                    Global.ChatGPTSocket = socket;
                    break;
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getCause()+ "Server port 번호를 잘못 입력하였습니다.");
        }
        catch(Exception e)
        {
            System.out.println(e.getCause() + "아직 처리 못한 오류 입니다.");
        }
    }
}



