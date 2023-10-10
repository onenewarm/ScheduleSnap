package api.mentalotus.Network;

import SocketSharedData.GptQuestion;
import SocketSharedData.GptResult;
import SocketSharedData.Header;
import api.mentalotus.Main.Global;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class ChatGptSession extends Session{
    public ChatGptSession(Socket socket, OutputStream sendBuffer, InputStream recvBuffer)
    {
        super(socket,sendBuffer,recvBuffer);
    }

    @Override
    public void OnRecv(Header header, Object recvData)
    {
        if (header.get_tag() == 1) {
            try
            {
                GptResult answer = (GptResult) recvData;
                Global.GGptResultMap.put(answer.getKey(), answer.getResult());
            } catch(Exception e)
            {
                System.out.println("최종 결과값 OnRecv에서 오류발생");
            }

        }
        else if(header.get_tag() < 0)
        {
            System.out.println("잘못된 태그 값 입니다.");
        }
    }

    @Override
    public void OnSend(Header header, Object sendData)
    {
        if(header.get_tag() == 2)
        {
            try
            {
                sendData = (GptQuestion)sendData;
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
