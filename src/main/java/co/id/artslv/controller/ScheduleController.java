package co.id.artslv.controller;


import co.id.artslv.lib.responses.MessageWrapper;
import co.id.artslv.lib.schedule.Schedule;
import co.id.artslv.lib.utility.CustomErrorResponse;
import co.id.artslv.lib.utility.CustomException;
import co.id.artslv.repository.InventoryRepository;
import co.id.artslv.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by gemuruhgeo on 06/09/16.
 */
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {
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


}
