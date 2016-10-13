package co.kereta.artslv;

import co.id.artslv.lib.availability.ScheduleData;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 03/10/16.
 */
public class ScheduleServiceTest {
    @Test
    public void deserialize() throws IOException {
        ScheduleData scheduleData = new ScheduleData();
        scheduleData.setNoka("ABC");

        ScheduleData scheduleData1 = new ScheduleData();
        scheduleData1.setNoka("ABC");

        Set<ScheduleData> test = new HashSet<>();
        test.add(scheduleData);
        test.add(scheduleData1);
        int size = test.size();
    }
}
