package co.id.artslv.repository;

import co.id.artslv.lib.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by root on 12/10/16.
 */
public interface TripRepository extends JpaRepository<Trip,String> {
    List<Trip> findByTripdate(LocalDate tripdate);
}
