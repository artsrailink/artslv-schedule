package co.id.artslv.repository;

import co.id.artslv.lib.allocation.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by root on 12/10/16.
 */
public interface AllocationRepository extends JpaRepository<Allocation,String> {
    List<Allocation> findBySchedulenokaAndStoporderorgAndStoporderdesAndTripdate(String noka, int orgstas, int deststas, LocalDate tripdate);
}
