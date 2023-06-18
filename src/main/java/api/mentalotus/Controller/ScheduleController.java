package api.mentalotus.Controller;
import api.mentalotus.Domain.Schedule;
import api.mentalotus.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController // controller임을 알려주는 표시
// 이곳으로 들어오는 API주소를 mapping, /api주소로 받겠다(localhost:8080/api)
public class ScheduleController {

    //    @RequestMapping(method = RequestMethod.POST, path = "/postMethod") // 아래랑 동일

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping(path = "/schedules", consumes = "application/json")
    public void save(@RequestBody Schedule schedule)
    {
        scheduleService.save(schedule);

    }

    @GetMapping(path = "/schedules")
    public List<Schedule> search(@RequestParam("userKey") String userKey)
    {
        List<Schedule> scheduleList = scheduleService.findUserSchedules(userKey);
        return scheduleList;
    }

    @DeleteMapping(path = "/schedules/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id)
    {
        scheduleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 스케줄이 삭제 되었습니다.");
    }

    @GetMapping(path = "/schedules/test")
    public List<Schedule> test()
    {
        Schedule A =new Schedule("schedule_key_1", "userkey", "벚꽃보러가기", "여자친구와의 첫 소풍이야!",
                "2023-06-18 10:30:00", "2023-06-18 21:30:00", "석촌호수");
        Schedule B =new Schedule("schedule_key_2", "userkey", "축구하기", "고등학교 친구들과 풋살 한판",
                "2023-06-20 9:30:00", "2023-06-20 17:30:00", "한경대 운동장");
        Schedule C =new Schedule("schedule_key_3", "userkey", "", "제목이 없는 경우입니다",
                "2023-06-21 9:30:00", "2023-06-21 17:30:00", "장소");
        Schedule D =new Schedule("schedule_key_4", "userkey", "내용이 없는 경우 입니다.",
                "", "2023-06-22 9:30:00", "2023-06-22 17:30:00", "장소");
        Schedule E =new Schedule("schedule_key_5", "userkey", "시간이 없는 경우 입니다.",
                "", "", "", "장소");
        Schedule F =new Schedule("schedule_key_6", "userkey", "장소가 없는 경우 입니다.",
                "", "2023-06-22 18:30:00", "2023-06-22 20:30:00", "");

        ArrayList<Schedule> schedules = new ArrayList<>();
        Collections.addAll(schedules, A,B,C,D,E,F);
        return schedules;
    }


}
