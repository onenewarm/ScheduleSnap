package api.mentalotus.Service;

import api.mentalotus.Domain.UploadImg;
import api.mentalotus.Macro.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static api.mentalotus.Macro.CoreMacro.processException;


@Service
@Setter
@Getter
public class ApiService {

    @Autowired
    public ApiService(){ };

    public ResponseEntity check()
    {
        if(processException(_image != null, "이미지를 불러오는데 실패했습니다."))
        {
            return _error("이미지를 불러오는데 실패했습니다.");
        }
        else if(processException(_image.getSize() < MAX_IMAGE_SIZE, "5MB 이하 이미지를 올려주세요."))
        {
            return _error("5MB 이하 이미지를 올려주세요.");
        }
        else
        {
            Init();
            return _success("이미지 업로드를 성공적으로 완수하였습니다.");
        }
    }


    public MultipartFile Send2Clova(String imgKey)
    {
        try{
            MultipartFile image = Global.Images.get(imgKey);
            Global.Images.remove(imgKey);
            SetImgStatus(imgKey,2);
            return image;
        } catch(NullPointerException e)
        {
            //TODO : NullPointer 즉 이미지가 만료 했으므로 다시 재업로드 해주어야 합니다.
        }
        return null;
    }

    public ResponseEntity SetImgStatus(String id, int status)
    {
        try{
            Global.SetStatus(id, status);
            return _success("Success");
        }catch(NullPointerException e)
        {
            return _error(e.getCause() + "이미지 데이터가 만료 됐습니다. 다시 업로드부터 진행해주세요.");
        }
    }

    private void Init()
    {
        UploadImg img = new UploadImg(1);
        Global.Images.put(img.get_id(),_image);
        Global.AddStatus(img.get_id(), img);
    }


    private ResponseEntity _error(String msg) {
        return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity _success(String msg) {
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    private MultipartFile _image;
    private int MAX_IMAGE_SIZE = 0x500000;

}
