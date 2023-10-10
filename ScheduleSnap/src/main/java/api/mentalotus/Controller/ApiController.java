package api.mentalotus.Controller;


import SocketSharedData.GptQuestion;
import SocketSharedData.Header;
import api.mentalotus.Main.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import api.mentalotus.Service.ApiService;

import static api.mentalotus.Network.SessionManager.GChatGptSession;
import static api.mentalotus.Service.ApiService.generateUniqueRandomKey;


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

    @PostMapping(path = "/gptQuestion")
    public String gpt_question(String question)
    {
        Header header = new Header(2);
        String key = generateUniqueRandomKey();
        GptQuestion gptQuestion = new GptQuestion(key, question);
        GChatGptSession.OnSend(header, gptQuestion);

        while(!Global.GGptResultMap.containsKey(key)) {}

        String result = Global.GGptResultMap.get(key);
        Global.GGptResultMap.remove(key);

        return result;
    }
}




