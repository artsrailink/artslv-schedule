package co.id.artslv.controller;


import co.id.artslv.lib.availability.AvailabilityData;
import co.id.artslv.lib.responses.MessageWrapper;
import co.id.artslv.lib.schedule.Schedule;
import co.id.artslv.lib.utility.CustomErrorResponse;
import co.id.artslv.lib.utility.CustomException;
import co.id.artslv.repository.InventoryRepository;
import co.id.artslv.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by gemuruhgeo on 06/09/16.
 */
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @RequestMapping(value = "/getall")
    public ResponseEntity<?> getAllSchedules(){
        try {
            MessageWrapper<List<Schedule>> listMessageWrapper = scheduleService.getAllSchedules();
            return new ResponseEntity<>(listMessageWrapper, HttpStatus.OK);
        } catch (CustomException e) {
            CustomErrorResponse errorResponse = (CustomErrorResponse)e.getCause();
            MessageWrapper<?> errorMessageWrapper = new MessageWrapper<>(errorResponse);
            return new ResponseEntity<>(errorMessageWrapper,HttpStatus.OK);
        }

    }
    @RequestMapping(value = "/getschedule/{origin}/{tripdate}",method = RequestMethod.GET)
    public ResponseEntity<?> getSchedule( @PathVariable String origin,
                                         @PathVariable String tripdate){
        LocalDate departdate = LocalDate.parse(tripdate,dateTimeFormatter);
        MessageWrapper<List<AvailabilityData>> availdatas = scheduleService.getScheduleAvail(departdate,origin,"");
        return new ResponseEntity<>(availdatas,HttpStatus.OK);
    }

    @RequestMapping(value = "/getschedule/{origin}/{destination}/{tripdate}",method = RequestMethod.GET)
    public ResponseEntity<?> getSchedule( @PathVariable String origin,
                                          @PathVariable String destination,
                                          @PathVariable String tripdate){
        LocalDate departdate = LocalDate.parse(tripdate,dateTimeFormatter);
        MessageWrapper<List<AvailabilityData>> availdatas = scheduleService.getScheduleAvail(departdate,origin,destination);
        return new ResponseEntity<>(availdatas,HttpStatus.OK);
    }


}
