package api.mentalotus.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import api.mentalotus.Service.ApiService;



@Controller
public class ApiController {
    ApiService apiservice;
    @PostMapping(path = "/upload/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity upload_image(@RequestParam MultipartFile imgFile)
    {
        return apiservice.check();
    }
}




