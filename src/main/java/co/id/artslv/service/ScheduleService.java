package co.id.artslv.service;

import co.id.artslv.lib.availability.AllocationData;
import co.id.artslv.lib.availability.AvailabilityData;
import co.id.artslv.lib.availability.ScheduleData;
import co.id.artslv.lib.responses.MessageWrapper;
import co.id.artslv.lib.schedule.PropertySchedule;
import co.id.artslv.lib.utility.CustomErrorResponse;
import co.id.artslv.lib.utility.CustomException;
import co.id.artslv.repository.PropScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = {CustomException.class}, propagation = Propagation.NESTED)
    public MessageWrapper<List<AvailabilityData>> getScheduleAvail(LocalDate departdate, String orgstasiun, String deststasiun) throws CustomException {
        List<PropertySchedule> propertySchedules = propScheduleRepository.findByTripdateAndStasiuncodeorgAndStasiuncodedesContainingIgnoreCase(departdate,orgstasiun,deststasiun);
        if(propertySchedules==null || propertySchedules.isEmpty()){
            throw new CustomException(new CustomErrorResponse("10","No Data Found"));
        }

        Set<AvailabilityData> availabilityDataSet = propertySchedules.stream().map(propertySchedule -> {
            AvailabilityData availabilityData = new AvailabilityData();
            availabilityData.setId(propertySchedule.getId());
            availabilityData.setTripdate(departdate);
            availabilityData.setStasiuncodeorg(propertySchedule.getStasiuncodeorg());
            availabilityData.setStasiuncodedes(propertySchedule.getStasiuncodedes());
            availabilityData.setStasiunnameorg(propertySchedule.getStasiunnameorg());
            return availabilityData;
        }).collect(Collectors.toSet());
        List<AvailabilityData> availabilityDatas = new ArrayList<>(availabilityDataSet);
        List<String> nokas = new ArrayList<>(propertySchedules.stream().map(propertySchedule -> propertySchedule.getNoka()).collect(Collectors.toSet()));
        List<String> subclass = new ArrayList<>(propertySchedules.stream().map(propertySchedule -> propertySchedule.getSubclasscode()).collect(Collectors.toSet()));

        for(AvailabilityData ad : availabilityDatas){
            List<ScheduleData> sds = new ArrayList<>();
            for(String noka:nokas){
                ScheduleData scheduleData = propertySchedules.stream().filter(ps->ps.getStasiuncodeorg().equals(ad.getStasiuncodeorg()) && ps.getStasiuncodedes().equals(ad.getStasiuncodedes()) && ps.getNoka().equals(noka)).map(ps$2->{
                    ScheduleData sd = new ScheduleData();
                    sd.setId(ps$2.getScheduleid());
                    sd.setTrainname(ps$2.getTrainname());
                    sd.setNoka(noka);
                    sd.setStopdeparture(ps$2.getStopdeparture());
                    sd.setStoparrival(ps$2.getStoparrival());
                    return sd;
                }).findFirst().get();
                List<AllocationData> allocationDatas = new ArrayList<>();
                for (String sc:subclass){
                    AllocationData alocData = propertySchedules.stream().filter(ps->ps.getStasiuncodeorg().equals(ad.getStasiuncodeorg()) && ps.getStasiuncodedes().equals(ad.getStasiuncodedes()) && ps.getNoka().equals(noka) && ps.getSubclasscode().equals(sc)).map(ps$2->{
                        AllocationData ald = new AllocationData();
                        ald.setSubclasscode(sc);
                        ald.setSeatavailable(ps$2.getSeatavailable());
                        ald.setFaretotamount(ps$2.getFaretotamount());
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
