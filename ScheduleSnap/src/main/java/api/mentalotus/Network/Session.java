package api.mentalotus.Network;
import SocketSharedData.Header;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
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
        //TODO
    }

    public void ProcessRecv()
    {
        while(true)
        {
            synchronized(this) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(_RecvBuffer);
                    ArrayList<Object> objects = (ArrayList<Object>) objectInputStream.readObject();
                    Header header = (Header) objects.get(0);
                    Object recvData = objects.get(1);
                    OnRecv(header, recvData);
                    Thread.sleep(10);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    System.out.println(e.getCause());
                }
            }
        }
    }

    public abstract void OnRecv(Header header, Object recvData);

    public abstract void OnSend(Header header, Object sendData);

}
