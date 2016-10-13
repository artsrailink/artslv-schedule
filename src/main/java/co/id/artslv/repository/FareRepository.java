package co.id.artslv.repository;

import co.id.artslv.lib.fare.Fare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by root on 11/10/16.
 */
public interface FareRepository extends JpaRepository<Fare,String>{
    List<Fare> findByStatusAndCodeorgAndCodedes(String status, String codeorg, String codedes);

    Fare findByStatusAndCodeorgAndCodedesAndSubclasscode(String status, String codeorg, String codedes, String subclasscode);
}
