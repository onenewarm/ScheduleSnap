package api.mentalotus.Network;

import SocketSharedData.Header;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClovaSession extends Session
{
    public ClovaSession(Socket socket, OutputStream sendBuffer, InputStream recvBuffer)
    {
        super(socket,sendBuffer,recvBuffer);
    }

    @Override
    public void OnRecv(Header header, Object recvData)
    {
        //TODO
    }

    @Override
    public void OnSend(Header header, Object sendData)
    {
        try{
            OutputStream outputStream  = SessionManager.GClovaSession.get_socket().getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(header);
            objects.add(sendData);
            objectOutputStream.writeObject(objects);
            System.out.println("Main서버에서 Clova로 보냄");
        } catch(Exception e)
        {
            //TODO : 오류 처리 필요
            System.out.println(e.getCause() + "무언가 오류 발생");
        }
    }
}
