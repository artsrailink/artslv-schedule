package co.id.artslv.controller;

import co.id.artslv.lib.availability.Availability;
import co.id.artslv.lib.response.MessageWrapper;
import co.id.artslv.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Created by root on 11/10/16.
 */
@RestController
@RequestMapping(value = "/schedule")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @RequestMapping(value = "/available",method = RequestMethod.POST)
    public ResponseEntity<?> getAvailability(@RequestBody Availability availability){
        String stasiunOrigin = availability.getStasiunorg();
        String stasiunDestination = availability.getStasiundest();
        LocalDate tripdate = availability.getTripdate();

        MessageWrapper<Object> messageWrapper = availabilityService.getAvailability(tripdate,stasiunOrigin,stasiunDestination);

        return new ResponseEntity<>(messageWrapper, HttpStatus.OK);
    }
}
