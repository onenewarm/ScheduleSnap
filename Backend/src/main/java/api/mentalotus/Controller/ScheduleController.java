package api.mentalotus.Controller;
import api.mentalotus.Domain.Schedule;
import api.mentalotus.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



}
