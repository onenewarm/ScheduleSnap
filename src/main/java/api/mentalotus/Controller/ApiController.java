package api.mentalotus.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import api.mentalotus.Service.ApiService;



@Controller
public class ApiController {

    @Autowired
    private ApiService apiService;
    @PostMapping(path = "/upload/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity upload_image(@RequestParam MultipartFile imgFile)
    {
        apiService.set_image(imgFile);
        return apiService.check();
    }

    @GetMapping(path = "/image")
    public MultipartFile get_image(@RequestParam("imgKey") String imgKey)
    {
        return apiService.Send2Clova(imgKey);
    }

    @GetMapping(path = "/isCompleted")
    public int isCompleted(@RequestParam("imgKey") String imgKey)
    {
        //TODO : 클라이언트가 현재 업로드한 이미에 대한 처리가 끝났는지 보는 것을 확인하는 코드
        return 0;
    }
}




