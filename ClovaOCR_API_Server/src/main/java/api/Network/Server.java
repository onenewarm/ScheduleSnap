package api.Network;

import SocketSharedData.Header;
import SocketSharedData.OcrResult;
import api.Utils.Global;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private Listener listener = new Listener(8888);
    public void Listen()
    {
        listener.Connect();
    }

    public static void ProcessServer()
    {
        System.out.println("ClovaServer의 ProcessServer 실행됨");
        while(true) {
            while (Global.OcrResults.isEmpty()) {  }
            System.out.println("ClovaServer의 OcrResults를 처리 했습니다.");
            Header header = new Header(1);
            OcrResult ocrResult;
            synchronized (Global.ocrLock) {
                ocrResult = Global.OcrResults.poll();
                System.out.println("OcrResults를 chatGPT로 보냈습니다.");
            }
            SessionManager.GChatGptSession.OnSend(header, ocrResult);
        }
    }
}

class ChatGptSession extends Session{

    public ChatGptSession(Socket _socket, OutputStream _SendBuffer, InputStream _RecvBuffer) {
        super(_socket, _SendBuffer, _RecvBuffer);
    }

    @Override
    public void OnRecv(Header header, Object recvData)
    {
        //TODO
    }

    @Override
    public void OnSend(Header header, Object sendData)
    {
        if(header.get_tag() == 1)
        {
            try
            {
                sendData = (OcrResult)sendData;
                OutputStream outputStream  = SessionManager.GChatGptSession.get_socket().getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(header);
                objects.add(sendData);
                objectOutputStream.writeObject(objects);
            }catch(Exception e)
            {
                System.out.println(e.getCause() + " ChatGpt로 OCRResult를 보내는 곳에서 문제가 발생했습니다.");
            }
        }
    }
}

class Listener {
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


            byte[] buffer = new byte[1024];

            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                String receivedData = new String(buffer, 0, bytesRead);
                System.out.println("클라이언트로부터 수신한 데이터: " + receivedData);

                if(receivedData.equals("CHATGPT"))
                {
                    System.out.println("CHATGPT Client 연결됨.");
                    SessionManager.GChatGptSession = new ChatGptSession(socket, socket.getOutputStream(),
                            socket.getInputStream());
                    return;
                }
                else
                {
                    System.out.println("오류");
                    throw new RuntimeException("Client 소켓 연결 시 버퍼에는 CHATGPT라고 버퍼에 입력해야 합니다.");
                }
            }

        }
        catch(IOException e)
        {
            System.out.println(e.getCause()+ " Client 소켓을 accept 하는 것을 실패 했습니다.");
        }
    }


}