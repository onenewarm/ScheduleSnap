package api.mentalotus.Service;

import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static api.mentalotus.Macro.CoreMacro.checkError;
import static api.mentalotus.Macro.CoreMacro.processException;

public class ApiService {
    public ApiService(MultipartFile image){
        _image = image;
    };

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
            return _success("이미지 업로드를 성공적으로 완수하였습니다.");
        }
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
