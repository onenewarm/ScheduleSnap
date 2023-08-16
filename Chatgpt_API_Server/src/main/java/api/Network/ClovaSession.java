package api.Network;

import SocketSharedData.GptResult;
import SocketSharedData.Header;
import SocketSharedData.OcrResult;
import api.Main.Global;
import api.Service.ServiceManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClovaSession extends Session
{
        public ClovaSession(Socket socket, OutputStream sendBuffer, InputStream recvBuffer)
        {
            super(socket,sendBuffer,recvBuffer);
        }
        @Override
        public void OnRecv(Header header, Object recvData)
        {
            System.out.println("ClovaOCR로부터 받은 데이터를 ChatGPT API에게 보냅니다.");
            if (header.get_tag() == 1) {
                try
                {
                    OcrResult ocrResult = (OcrResult) recvData;
                    System.out.println("ChatGPT에게 ClovaOCR 데이터를 줍니다.");
                    String answer = ServiceManager.GChatGPTService.ask(ocrResult.getResult());
                    synchronized (Global.CHATGPTLock)
                    {
                        GptResult gptResult = new GptResult(ocrResult.getKey(), answer);
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
            //TODO
        }
}
