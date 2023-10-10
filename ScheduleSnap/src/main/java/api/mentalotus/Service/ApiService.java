package api.mentalotus.Service;

import SocketSharedData.Header;
import api.mentalotus.Domain.UploadImg;
import SocketSharedData.ScheduleIMG;
import api.mentalotus.Network.SessionManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

import static api.mentalotus.Macro.CoreMacro.processException;


@Service
@Setter
@Getter
public class ApiService {

    @Autowired
    public ApiService(){ };

    public String RegisterImg(MultipartFile img)
    {
        String key = "";
        if (img == null) {
            System.out.println("이미지가 비었네요.");
        }
        try{
            String filename = img.getOriginalFilename();
            String[] parts = filename.split("\\.");
            String extension = parts[parts.length - 1];

            byte[] covetedImg = img.getBytes();
            _check(covetedImg);
            key = generateUniqueRandomKey();
            UploadImg imgInfo = new UploadImg(key);
            Send2Clova(covetedImg, imgInfo.get_id(), extension);
        } catch(Exception e)
        {

        }

        return key;

    }

    private void _check(byte[] _image)
    {
        if(processException(_image != null, "이미지를 불러오는데 실패했습니다."))
        {
            throw new NullPointerException("이미지를 불러오는데 실패했습니다.");
        }
        else if(processException(_image.length < MAX_IMAGE_SIZE, "5MB 이하 이미지를 올려주세요."))
        {
            throw new IllegalArgumentException("5MB이하의 이미지만 업로드 할 수 있습니다.");
        }

    }
    public static String generateUniqueRandomKey() {
        Random random = new Random();
        int randomNumber;

        long currentTimeMillis = System.currentTimeMillis();
        random.setSeed(currentTimeMillis);
        randomNumber = random.nextInt();
        if(randomNumber < 0){
            randomNumber = randomNumber >>> 0;
        }
        String res = Integer.toString(randomNumber);

        return res;
    }

    public void Send2Clova(byte[] img, final String imgKey, final String extension)
    {
        try{
            Header header = new Header(1);
            ScheduleIMG sendData = new ScheduleIMG(img,imgKey,extension);
            SessionManager.GClovaSession.OnSend(header, sendData);
        } catch(NullPointerException e)
        {
            throw new NullPointerException("이미지가 만료 했으므로 다시 재업로드 해주어야 합니다.");
        }
    }

    private int MAX_IMAGE_SIZE = 0x500000;

}
