package api.Network;

import SocketSharedData.Header;
import SocketSharedData.OcrResult;
import SocketSharedData.ScheduleIMG;
import api.Utils.Global;
import api.Service.ServiceManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;


public class Client {
    public void Connect()
    {
        try{
            Socket socket = new Socket(Global.YMLReader.getServerConfig().getMAINSERVERIP(),
                    Global.YMLReader.getServerConfig().getMAINSERVERPORT());
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            ClovaSession session = new ClovaSession(socket, out, in);
            SessionManager.GMainServerSession = session;

            System.out.println("TEST Client 연결 성공");
            String outputMessage = "CLOVA";
            byte[] data = outputMessage.getBytes();
            out.write(data);
            out.flush();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            try{
                Thread.sleep(3000);
            } catch(Exception sleepE)
            {

            }
            Connect();
        }
    }

    public static void ProcessClient()
    {

        while(true) {
            SessionManager.GMainServerSession.ProcessRecv();
        }
    }
}

class ClovaSession extends Session
{
    public ClovaSession(Socket socket, OutputStream sendBuffer, InputStream recvBuffer)
    {
        super(socket,sendBuffer,recvBuffer);
    }
    @Override
    public void OnRecv(Header header, Object recvData)
    {
        if (header.get_tag() == 1) {
            try
            {
                ScheduleIMG scheduleIMG = (ScheduleIMG) recvData;
                String format = scheduleIMG.get_extension();
                String name = scheduleIMG.get_scheduleKey();

                byte[] _bytesimg = scheduleIMG.get_img();
                byte[] encodedBytes = Base64.getEncoder().encode(_bytesimg);
                String data = new String(encodedBytes);

                String _result = ServiceManager.GClovaService.processOCR(format,data,name);
                synchronized (Global.ocrLock)
                {
                    OcrResult ocrResult = new OcrResult(scheduleIMG.get_scheduleKey(), _result);
                    Global.OcrResults.add(ocrResult);
                }

                System.out.println(scheduleIMG.get_extension());
            } catch(Exception e)
            {
                //TODO : 오류처리
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
        //TODO
    }
}
