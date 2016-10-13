package co.id.artslv.service;

import co.id.artslv.lib.availability.Availability;
import co.id.artslv.lib.availability.Availability$2;
import co.id.artslv.lib.inventory.Inventory;
import co.id.artslv.lib.payments.Fare;
import co.id.artslv.lib.responses.MessageWrapper;
import co.id.artslv.lib.schedule.Allocation;
import co.id.artslv.lib.schedule.Schedule;
import co.id.artslv.lib.schedule.Stop;
import co.id.artslv.lib.schedule.Trip;
import co.id.artslv.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by root on 11/10/16.
 */
@Service
public class AvailabilityService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private FareRepository fareRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    public MessageWrapper<Availability$2> getAvailability(String reqid, LocalDate tripdate, String orgstasiun, String deststasiun){
        List<Trip> trips = tripRepository.findByTripdate(tripdate);

        Availability$2 availability$2 = new Availability$2();
        availability$2.setOrgstasiun(orgstasiun);
        availability$2.setDeststasiun(deststasiun);
        availability$2.setTripdate(tripdate);


        List<Schedule> schedules = trips.stream().map(trip -> scheduleRepository.findByNoka(trip.getSchedulenoka())).collect(Collectors.toList());
        Map<String,String> unavailableNoka = new HashMap<>();
        for(Schedule schedule: schedules){

            String noka = schedule.getNoka();
            Stop arivingStop = stopRepository.findByStatsiuncodeAndSchedulenoka(deststasiun,noka);
            Stop departStop = stopRepository.findByStatsiuncodeAndSchedulenoka(orgstasiun,noka);

            if(arivingStop==null || departStop==null){
                unavailableNoka.put(noka,"NaN");
                continue;
            }

            schedule.setDeparttime(departStop.getStopdeparture());
            schedule.setArivingtime(arivingStop.getStoparrival());

            int orgOrder = departStop.getStoporder();
            int destOrder = arivingStop.getStoporder();

            int orderBenchmark = orgOrder<destOrder?orgOrder:destOrder;
            if(orgOrder>destOrder){
                int temp = orgOrder;
                orgOrder = destOrder;
                destOrder = temp;
            }

            List<Inventory> benchmarkOriginInventory = inventoryRepository.findByBookstatAndTripdateAndSchedulenokaAndStoporder("0",tripdate,noka,orderBenchmark);
            List<Inventory> occupiedSeatBetweenOrgNDest = inventoryRepository.findByBookstatAndTripdateAndSchedulenokaAndStoporderBetween("1",tripdate,noka,orgOrder,destOrder);

            occupiedSeatBetweenOrgNDest.forEach(inventory -> {
                benchmarkOriginInventory.removeIf(p->p.getWagondetid().equals(inventory.getWagondetid()) && p.getStamformdetcode().equals(inventory.getStamformdetcode()));

            });

            int availableseat = benchmarkOriginInventory.size();
            schedule.setAvailableseat(availableseat);

            List<Allocation> allocations = allocationRepository.findBySchedulenokaAndStoporderorgAndStoporderdesAndTripdate(noka,orgOrder,destOrder,tripdate);
            allocations.forEach(allocation -> {
                Fare fare = fareRepository.findByStatusAndCodeorgAndCodedesAndSubclasscode("1",allocation.getStasiuncodeorg(),allocation.getStasiuncodedes(),allocation.getSubclasscode());
                allocation.setFare(fare);
            });

            schedule.setAllocations(allocations);
        }

        schedules.removeIf(schedule -> unavailableNoka.get(schedule.getNoka())!=null && unavailableNoka.get(schedule.getNoka()).equals("NaN"));

        availability$2.setSchedules(schedules);
        MessageWrapper<Availability$2> result = new MessageWrapper<>("00","SUCCESS",availability$2);
        return result;

    }

    public MessageWrapper<Object> getAvailability(LocalDate tripdate,String orgstasiun,String deststasiun){

        List<Stop> stopOrigin = stopRepository.findByStatsiuncode(orgstasiun);
        List<Stop> stopDestination = stopRepository.findByStatsiuncode(deststasiun);
        int orgOrder = stopOrigin.get(0).getStoporder();
        int destOrder = stopDestination.get(0).getStoporder();

        int orderBenchmark = orgOrder<destOrder?orgOrder:destOrder;
        if(orgOrder>destOrder){
            int temp = orgOrder;
            orgOrder = destOrder;
            destOrder = temp;
        }

        List<Fare> fares = fareRepository.findByStatusAndCodeorgAndCodedes("1",orgstasiun,deststasiun);

        List<Inventory> benchmarkOriginInventory = inventoryRepository.findByBookstatAndTripdateAndStoporder("0",tripdate,orderBenchmark);
        List<Inventory> occupiedSeatBetweenOrgNDest = inventoryRepository.findByBookstatAndTripdateAndStoporderBetween("1",tripdate,orgOrder,destOrder);
        //Remove data occupied seat from benchmark
        occupiedSeatBetweenOrgNDest.forEach(inventory -> {
            benchmarkOriginInventory.removeIf(p->p.getWagondetid().equals(inventory.getWagondetid()) && p.getStamformdetcode().equals(inventory.getStamformdetcode()));
        });

        List<Inventory> availableSeats = new ArrayList<>();
        benchmarkOriginInventory.forEach(inventory -> {
            Inventory availableSeat = new Inventory();
            availableSeat.setStatus(inventory.getStatus());
            availableSeat.setBookstat(inventory.getBookstat());
            availableSeat.setSchedulenoka(inventory.getSchedulenoka());
            availableSeat.setTripdate(inventory.getTripdate());
            availableSeat.setWagondetcol(inventory.getWagondetcol());
            availableSeat.setWagondetid(inventory.getWagondetid());
            availableSeat.setWagondetrow(inventory.getWagondetrow());
            availableSeat.setStamformdetcode(inventory.getStamformdetcode());
            availableSeat.setStamformdetid(inventory.getStamformdetid());

            availableSeats.add(availableSeat);
        });

        Availability availability = new Availability();
        availability.setStasiunorg(orgstasiun);
        availability.setStasiundest(deststasiun);
        availability.setAvailable(availableSeats);

        MessageWrapper<Object> messageWrapper = new MessageWrapper<>("00","SUCCESS",availability,fares);
        return messageWrapper;
    }
}
