package api.mentalotus.Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;



public class Listener {
    private ServerSocket serverSocket;
    private Socket socket;

    public Listener(int port)
    {
        try{
            serverSocket = new ServerSocket(port);
        }

        catch(Exception e)
        {
            System.out.println(e.getCause() + "처리 못한 오류 입니다. 오류 예외 처리를 해주시기 바랍니다.");
        }
    }

    public void Connect()
    {
        InputStream in;
        try{
            System.out.println("[Server실행] Client연결대기중...");
            socket = serverSocket.accept();			// 연결대기
            in = socket.getInputStream();
            System.out.println("Client 연결됨.");

            byte[] buffer = new byte[1024];

            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                String receivedData = new String(buffer, 0, bytesRead);
                System.out.println("클라이언트로부터 수신한 데이터: " + receivedData);

                if(receivedData.equals("CLOVA"))
                {
                    SessionManager.GClovaSession = new ClovaSession(socket, socket.getOutputStream(),
                            socket.getInputStream());
                    return;
                }
                else if(receivedData.equals("CHATGPT"))
                {
                    SessionManager.GChatGptSession = new ChatGptSession(socket, socket.getOutputStream(),
                            socket.getInputStream());
                    return;
                }
                else
                {
                    System.out.println("오류");
                    throw new RuntimeException("Client 소켓 연결 시 버퍼에는 CLOVA 또는 CHATGPT이어야 합니다.");
                }
            }

        }
        catch(IOException e)
        {
            System.out.println(e.getCause()+ " Client 소켓을 accept 하는 것을 실패 했습니다.");
        }
    }


}



