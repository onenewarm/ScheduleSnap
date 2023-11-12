package api.mentalotus.Controller;


import SocketSharedData.GptQuestion;
import SocketSharedData.Header;
import api.mentalotus.Main.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import api.mentalotus.Service.ApiService;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

import static api.mentalotus.Network.SessionManager.GChatGptSession;
import static api.mentalotus.Service.ApiService.generateUniqueRandomKey;


@RestController
public class ApiController {
    @Autowired
    private ApiService apiService;
    @PostMapping(path = "/upload/image")
    public CompletableFuture<String> upload_image(@RequestPart(value = "imgFile") MultipartFile imgFile)
    {
        System.out.println(imgFile.getOriginalFilename());
        String key = apiService.RegisterImg(imgFile);
        CompletableFuture<String> resultFuture = apiService.waitForResult(key);
        return resultFuture;
    }

    @PostMapping(path = "/gptQuestion")
    public CompletableFuture<String> gpt_question(String question)
    {
        Header header = new Header(2);
        String key = generateUniqueRandomKey();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        question += " 답변은 50자 이내로 해주세요." + "하나의 결과는 10글자 이내로 작성해주세요.\n" +
                "seed 값을" + timestamp + key +"로 설정하여 답변해주세요.";;
        GptQuestion gptQuestion = new GptQuestion(key, question);
        GChatGptSession.OnSend(header, gptQuestion);
        CompletableFuture<String> resultFuture = apiService.waitForResult(key);
        return resultFuture;
    }

    @PostMapping(path = "/dailyMission")
    public CompletableFuture<String> dailyMission(String category, int Count)
    {
        Header header = new Header(2);
        String key = generateUniqueRandomKey();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String question = category + "과 관련된 오늘의 Todo List를 " + Count + "개 만들어줘.\n" +
                "하나의 결과는 10글자 이내로 작성해주세요.\n" +
                "seed 값을" + timestamp + key +"로 설정하여 답변해주세요.";
        GptQuestion gptQuestion = new GptQuestion(key, question);
        GChatGptSession.OnSend(header, gptQuestion);
        CompletableFuture<String> resultFuture = apiService.waitForResult(key);
        return resultFuture;
    }

    @PostMapping(path = "/textToSchedule")
    public CompletableFuture<String> textToSchedule(String text)
    {
        Header header = new Header(2);
        String key = generateUniqueRandomKey();
        String question = text + "I'm going to make a JSON type. Please do the format of JSON type as I wrote it.\n " +
        "json cannot contain special characters in the value.\n" +
        "And make sure to match the 24-hour format (ex, 'hh:mm', '15:44', and '21:00') when marking the time\n" +
        "When expressing the date, it must be written in accordance with (Format: YYYY년 MM월 DD일),(ex: '2022년 03월 08일', '1999년 10월 26일' ).\n" +
        "json format : \n" +
        "{\n" +
        "    Title : (Provide a title for the writing. Note that it should not be in the form of a sentence.)\n"+
        "    Contents : (Write summarized the contents of the writing. Constraints : 30 words or less)\n" +
        "    Category1 : You must select value in examples.(examples: 기념일/공연/대회/축제/운동/모임/여가/예약/공부/업무/기타), (Look at Title and choose Category. If you select 기타, look at the Contents and choose.)\n" +
        "    StartDate : (Please include the start date of the schedule. If there is no relevant date, please leave it empty.Constraints : (Format YYYY년 MM월 DD일)\n" +
        "    StartTime : (If there is a time associated with the date you entered 'StartDate', Constraints : the time in 24-hour format, otherwise, leave it blank)\n" +
        "    EndDate : (Please include the end date of the schedule. If there is no relevant date, please leave it empty. Constraints : (Format YYYY년 MM월 DD일)\n" +
        "    EndTime :(If there is a time associated with the date you entered 'EndDate', Constraints : the time in 24-hour format (e.g., '20:00'); otherwise, leave it blank)\n" +
        "    Place : (Please put in a place related to the writing If there is no related place, leave it empty )\n"+
        "    URL : (If there is a URL form related to the writing, please put it in. And if you don't have it, please empty it.)\n"+
        "}\n"+
        "답변은 json형식만 나와야 합니다. json형식 외에 다른 comment는 작성하지 말아주세요.\n";
        GptQuestion gptQuestion = new GptQuestion(key, question);
        GChatGptSession.OnSend(header, gptQuestion);
        CompletableFuture<String> resultFuture = apiService.waitForResult(key);
        return resultFuture;
    }
}




