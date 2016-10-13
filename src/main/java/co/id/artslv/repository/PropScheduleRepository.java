package co.id.artslv.repository;

import co.id.artslv.lib.schedule.PropertySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by root on 13/10/16.
 */
public interface PropScheduleRepository extends JpaRepository<PropertySchedule,String>{
    List<PropertySchedule> findByTripdateAndStasiuncodeorgAndStasiuncodedes(LocalDate tglbrangkat,String orgstasiun,String deststasiun);
    List<PropertySchedule> findByTripdateAndStasiuncodeorgAndStasiuncodedesContainingIgnoreCase(LocalDate tglbrangkat,String orgstasiun,String deststasiun);
    List<PropertySchedule> findByTripdateAndStasiuncodeorg(LocalDate tglbrangkat,String orgstasiun);
    List<PropertySchedule> findByTripdateAndStasiuncodeorgAndNoka(LocalDate tglbrangkat,String orgstasiun,String noka);

    List<PropertySchedule> findByTripdateAndStasiuncodeorgAndStasiuncodedesAndNoka(LocalDate tglbrangkat,String orgstasiun,String deststasiun,String noka);


}
