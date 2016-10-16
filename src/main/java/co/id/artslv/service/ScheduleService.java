package co.id.artslv.service;

import co.id.artslv.lib.availability.AllocationData;
import co.id.artslv.lib.availability.AvailabilityData;
import co.id.artslv.lib.availability.ScheduleData;
import co.id.artslv.lib.responses.MessageWrapper;
import co.id.artslv.lib.schedule.PropertySchedule;
import co.id.artslv.repository.PropScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by root on 26/09/16.
 */
@Service
public class ScheduleService {

    @Autowired
    private PropScheduleRepository propScheduleRepository;


    public MessageWrapper<List<AvailabilityData>> getScheduleAvail(LocalDate departdate, String orgstasiun, String deststasiun){
        List<PropertySchedule> propertySchedules = propScheduleRepository.findByTripdateAndStasiuncodeorgAndStasiuncodedesContainingIgnoreCase(departdate,orgstasiun,deststasiun);

        Set<AvailabilityData> availabilityDataSet = propertySchedules.stream().map(propertySchedule -> {
            AvailabilityData availabilityData = new AvailabilityData();
            availabilityData.setDepartdate(departdate);
            availabilityData.setStationOrg(propertySchedule.getStasiuncodeorg());
            availabilityData.setStationDest(propertySchedule.getStasiuncodedes());
            return availabilityData;
        }).collect(Collectors.toSet());
        List<AvailabilityData> availabilityDatas = new ArrayList<>(availabilityDataSet);
        List<String> nokas = new ArrayList<>(propertySchedules.stream().map(propertySchedule -> propertySchedule.getNoka()).collect(Collectors.toSet()));
        List<String> subclass = new ArrayList<>(propertySchedules.stream().map(propertySchedule -> propertySchedule.getSubclasscode()).collect(Collectors.toSet()));

        for(AvailabilityData ad : availabilityDatas){
            List<ScheduleData> sds = new ArrayList<>();
            for(String noka:nokas){
                ScheduleData scheduleData = propertySchedules.stream().filter(ps->ps.getStasiuncodeorg().equals(ad.getStationOrg()) && ps.getStasiuncodedes().equals(ad.getStationDest()) && ps.getNoka().equals(noka)).map(ps$2->{
                    ScheduleData sd = new ScheduleData();
                    sd.setNoka(noka);
                    sd.setJambrangkat(ps$2.getStopdeparture());
                    sd.setJamsampai(ps$2.getStoparrival());
                    return sd;
                }).findFirst().get();
                List<AllocationData> allocationDatas = new ArrayList<>();
                for (String sc:subclass){
                    AllocationData alocData = propertySchedules.stream().filter(ps->ps.getStasiuncodeorg().equals(ad.getStationOrg()) && ps.getStasiuncodedes().equals(ad.getStationDest()) && ps.getNoka().equals(noka) && ps.getSubclasscode().equals(sc)).map(ps$2->{
                        AllocationData ald = new AllocationData();
                        ald.setSubclass(sc);
                        ald.setSisakursi(ps$2.getSeatavailable());
                        ald.setFare(ps$2.getTotamount());
                        return ald;
                    }).findFirst().get();

                    allocationDatas.add(alocData);
                }
                scheduleData.setAllocationDatas(allocationDatas);
                sds.add(scheduleData);
            }
            ad.setScheduleDatas(sds);
        }

        MessageWrapper<List<AvailabilityData>> result = new MessageWrapper<>("00","SUCCESS",availabilityDatas);
        return result;
    }

}
