package co.id.artslv.service;

import co.id.artslv.lib.availability.Availability;
import co.id.artslv.lib.fare.Fare;
import co.id.artslv.lib.inventory.Inventory;
import co.id.artslv.lib.response.MessageWrapper;
import co.id.artslv.lib.stop.Stop;
import co.id.artslv.repository.FareRepository;
import co.id.artslv.repository.InventoryRepository;
import co.id.artslv.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
