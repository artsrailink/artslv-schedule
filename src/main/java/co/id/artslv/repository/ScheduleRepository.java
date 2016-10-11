package co.id.artslv.repository;

import co.id.artslv.lib.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 26/09/16.
 */
public interface ScheduleRepository extends JpaRepository<Schedule,String>{
    Schedule findById(String scheduleid);
}
