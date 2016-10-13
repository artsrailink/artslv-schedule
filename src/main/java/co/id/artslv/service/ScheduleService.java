package co.id.artslv.service;

import co.id.artslv.lib.availability.AllocationData;
import co.id.artslv.lib.availability.AvailabilityData;
import co.id.artslv.lib.availability.ScheduleData;
import co.id.artslv.lib.responses.MessageWrapper;
import co.id.artslv.lib.schedule.PropertySchedule;
import co.id.artslv.lib.schedule.Schedule;
import co.id.artslv.lib.utility.CustomErrorResponse;
import co.id.artslv.lib.utility.CustomException;
import co.id.artslv.repository.PropScheduleRepository;
import co.id.artslv.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by root on 26/09/16.
 */
@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PropScheduleRepository propScheduleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = CustomException.class)
    public MessageWrapper<List<Schedule>> getAllSchedules() throws CustomException {
        List<Schedule> schedules = scheduleRepository.findAll();
        if(schedules==null){
            throw new CustomException(new CustomErrorResponse("10","Schedule Not Find"));
        }
        MessageWrapper<List<Schedule>> scheduleWrapper = new MessageWrapper<>("00","SUCCESS",schedules);
        return scheduleWrapper;
    }

    public MessageWrapper<List<AvailabilityData>> getScheduleAvail(LocalDate departdate,String orgstasiun,String deststasiun){
        List<PropertySchedule> propertySchedules = propScheduleRepository.findByTripdateAndStasiuncodeorgAndStasiuncodedesContainingIgnoreCase(departdate,orgstasiun,deststasiun);

        List<String> destinations = propertySchedules.stream().map(propertySchedule -> propertySchedule.getStasiuncodedes()).collect(Collectors.toList());;
        List<String> destNoDuplicte = new ArrayList<>(new HashSet<>(destinations));
        List<AvailabilityData> availabilityDatas = new ArrayList<>();
        for(String dest:destNoDuplicte){
            AvailabilityData availabilityData = new AvailabilityData();
            availabilityData.setStationDest(dest);
            availabilityData.setStationOrg(orgstasiun);
            availabilityData.setDepartdate(departdate);
            availabilityDatas.add(availabilityData);
        }
        availabilityDatas.forEach(availabilityData -> {
            List<PropertySchedule> ps2 = propScheduleRepository.findByTripdateAndStasiuncodeorgAndStasiuncodedes(availabilityData.getDepartdate(),availabilityData.getStationOrg(),availabilityData.getStationDest());
            Set<ScheduleData> scheduleDatas = ps2.stream().map(p->{
                ScheduleData scheduleData = new ScheduleData();
                scheduleData.setNoka(p.getNoka());
                scheduleData.setJambrangkat(p.getStopdeparture());
                scheduleData.setJamsampai(p.getStoparrival());
                return scheduleData;
            }).collect(Collectors.toSet());

            availabilityData.setScheduleDatas(new ArrayList<>(scheduleDatas));

        });

        availabilityDatas.forEach(ad->{
            List<ScheduleData> scheduleDatas = ad.getScheduleDatas();
            scheduleDatas.forEach(sc->{
                List<PropertySchedule> ps3 = propScheduleRepository.findByTripdateAndStasiuncodeorgAndStasiuncodedesAndNoka(ad.getDepartdate(),ad.getStationOrg(),ad.getStationDest(),sc.getNoka());
                List<AllocationData> allocationDatas = ps3.stream().map(p->{
                    AllocationData allocationData = new AllocationData();
                    allocationData.setFare(p.getTotamount());
                    allocationData.setSisakursi(p.getSeatavailable());
                    allocationData.setSubclass(p.getSubclasscode());
                    return allocationData;
                }).collect(Collectors.toList());

                sc.setAllocationDatas(allocationDatas);
            });
        });

        MessageWrapper<List<AvailabilityData>> result = new MessageWrapper<>("00","SUCCESS",availabilityDatas);
        return result;
    }

}
