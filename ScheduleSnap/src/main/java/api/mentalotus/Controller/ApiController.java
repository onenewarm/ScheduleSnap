package api.mentalotus.Controller;


import api.mentalotus.Main.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import api.mentalotus.Service.ApiService;


@RestController
public class ApiController {

    @Autowired
    private ApiService apiService;
    @PostMapping(path = "/upload/image")
    public String upload_image(@RequestPart(value = "imgFile") MultipartFile imgFile)
    {
        System.out.println(imgFile.getOriginalFilename());
        String key = apiService.RegisterImg(imgFile);
        while(!Global.GGptResultMap.containsKey(key)) {}

        String result = Global.GGptResultMap.get(key);
        Global.GGptResultMap.remove(key);

        return result;
    }
}




