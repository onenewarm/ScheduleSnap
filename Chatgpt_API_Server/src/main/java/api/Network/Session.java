package api.Network;

import SocketSharedData.Header;
import api.Main.Global;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
public abstract class Session{
    private Socket _socket;
    private OutputStream _SendBuffer;
    private InputStream _RecvBuffer;


    public void ProcessSend()
    {
        while(true)
        {
            try{
                OnSend();
            } catch(Exception e){}
        }
    }

    public void ProcessRecv()
    {
        System.out.println("ChatGpt ProcessRecv 실행됨");
        while(true)
        {
            Header header = null;
            Object recvData = null;
            synchronized(this) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(_RecvBuffer);
                    ArrayList<Object> objects = (ArrayList<Object>) objectInputStream.readObject();
                    header = (Header) objects.get(0);
                    recvData = objects.get(1);
                } catch (IOException e) {
                    System.out.println(e.getCause());
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    System.out.println(e.getCause());
                }
            }
            if(header != null) OnRecv(header, recvData);
        }
    }

    public abstract void OnRecv(Header header, Object recvData);

    public abstract void OnSend();
}
