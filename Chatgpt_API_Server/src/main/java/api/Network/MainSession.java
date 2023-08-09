package api.Network;

import SocketSharedData.GptResult;
import SocketSharedData.Header;
import api.Main.Global;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MainSession extends Session
{
    public MainSession(Socket socket, OutputStream sendBuffer, InputStream recvBuffer)
    {
        super(socket,sendBuffer,recvBuffer);
    }
    @Override
    public void OnRecv(Header header, Object recvData)
    {
        if (header.get_tag() == 1) {
            try
            {

            } catch(Exception e)
            {
                System.out.println("왓나?");
            }

        }
        else if(header.get_tag() < 0)
        {
            System.out.println("잘못된 태그 값 입니다.");
        }
    }

    @Override
    public void OnSend()
    {
        while(Global.CHATGPTResults.isEmpty()) { }
        Header header = new Header(1);
        GptResult ChatGptResult;
        synchronized (Global.CHATGPTLock) {
            ChatGptResult = Global.CHATGPTResults.poll();
        }
        if(header.get_tag() == 1)
        {
            try
            {
                OutputStream outputStream  = SessionManager.GMainServerSession.get_socket().getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(header);
                objects.add(ChatGptResult);
                objectOutputStream.writeObject(objects);
            } catch(Exception e)
            {
                System.out.println("ChatGptResults 큐에서 값을 가져와서 처리하는데에 문제가 발생했습니다.");
            }
        }
    }
}
