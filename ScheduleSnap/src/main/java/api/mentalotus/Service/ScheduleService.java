package api.mentalotus.Service;

import api.mentalotus.Domain.Schedule;
import api.mentalotus.Repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository)
    {
        this.scheduleRepository = scheduleRepository;
    }

    public void save(Schedule schedule){
        scheduleRepository.save(schedule);
    }

    public void delete(String id) {scheduleRepository.deleteById(id);}

    public List<Schedule> findUserSchedules(String userKey)
    {
        return scheduleRepository.findByUserKey(userKey);
    }
}
