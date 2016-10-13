package co.id.artslv.controller;

import co.id.artslv.lib.availability.Availability;
import co.id.artslv.lib.availability.Availability$2;
import co.id.artslv.lib.responses.MessageWrapper;
import co.id.artslv.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by root on 11/10/16.
 */
@RestController
@RequestMapping(value = "/schedule")
public class AvailabilityController {

    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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

    @RequestMapping(value = "/getschedule2/{reqid}/{origin}/{destination}/{tripdate}",method = RequestMethod.GET)
    public ResponseEntity<?> getSchedule(@PathVariable String reqid, @PathVariable String origin, @PathVariable String destination,
                                         @PathVariable String tripdate){


        LocalDate departdate = LocalDate.parse(tripdate,dateTimeFormatter);

        MessageWrapper<Availability$2> result = availabilityService.getAvailability(reqid,departdate,origin,destination);
        return new ResponseEntity<>(result,HttpStatus.OK);

    }
}
