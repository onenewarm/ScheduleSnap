package api.Network;

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
public abstract class Session {
    private Socket _socket;
    private OutputStream _SendBuffer;
    private InputStream _RecvBuffer;


    public void ProcessSend()
    {
        //TODO : Socket을 통해서 데이터를 보내는 코드를 작성 해야 합니다.
    }

    public void ProcessRecv()
    {
        while(true)
        {
            synchronized(this) {
                try {
                    OutputStream os = _SendBuffer;
                    InputStream is = _RecvBuffer;

                    ObjectInputStream ois = new ObjectInputStream(is);
                    ArrayList<Object> objects = (ArrayList<Object>) ois.readObject();
                    Header header = (Header) objects.get(0);
                    Object recvData = objects.get(1);
                    OnRecv(header, recvData);
                } catch (IOException e) {
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public abstract void OnRecv(Header header, Object recvData);

    public abstract void OnSend(Header header, Object sendData);
}
