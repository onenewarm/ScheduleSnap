package api.Network;

import SocketSharedData.GptQuestion;
import SocketSharedData.GptResult;
import SocketSharedData.Header;
import SocketSharedData.OcrResult;
import api.Main.Global;
import api.Service.ServiceManager;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MainSession extends Session
{
    public MainSession(Socket socket, OutputStream sendBuffer, InputStream recvBuffer)
    {
        super(socket,sendBuffer,recvBuffer);
    }
    @Override
    public void OnRecv(Header header, Object recvData)
    {
        if (header.get_tag() == 2) {
            try
            {
                GptQuestion gptQuestion = (GptQuestion) recvData;
                System.out.println("질문생성ai");
                CompletableFuture<String> future = ServiceManager.GChatGPTService.qask(gptQuestion.getQuestion());
                String answer = future.get();
                synchronized (Global.CHATGPTLock)
                {
                    GptResult gptResult = new GptResult(gptQuestion.getKey(), answer);
                    Global.CHATGPTResults.add(gptResult);
                }
            } catch(Exception e)
            {
                System.out.println("OCR 결과를 ChatGpt로 보내는 과정에서 문제 발생");
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
