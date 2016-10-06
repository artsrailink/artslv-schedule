package co.id.artslv.service;

import co.id.artslv.lib.response.MessageWrapper;
import co.id.artslv.lib.schedule.Schedule;
import co.id.artslv.lib.utility.CustomErrorResponse;
import co.id.artslv.lib.utility.CustomException;
import co.id.artslv.repository.ScheduleRepository;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by root on 26/09/16.
 */
@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

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
    @Transactional(rollbackFor = CustomException.class)
    public String testGetFunction(String name){
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
        simpleJdbcCall.withFunctionName("F_GEO_TEST");
        simpleJdbcCall.declareParameters(new SqlParameter("na", OracleTypes.VARCHAR));
        String result = simpleJdbcCall.executeFunction(String.class);
        return result;
    }

}
