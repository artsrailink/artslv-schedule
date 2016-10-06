package co.kereta.artslv;

import co.id.artslv.lib.response.MessageWrapper;
import co.id.artslv.lib.transactions.Transaction;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by root on 03/10/16.
 */
public class ScheduleServiceTest {
    @Test
    public void deserialize() throws IOException {
        String jsondata = "{\n" +
                "\t\"response\": {\n" +
                "\t\t\"transactionlist\": [{\n" +
                "\t\t\t\"id\": \"14208695-2135-4b2f-afdd-9f416174fe1d\",\n" +
                "\t\t\t\"date\": \"2016-07-24 00:00:00\",\n" +
                "\t\t\t\"departdate\": \"2016-10-05\",\n" +
                "\t\t\t\"paycode\": \"1218722620638\",\n" +
                "\t\t\t\"totamount\": 1200000,\n" +
                "\t\t\t\"status\": \"1\",\n" +
                "\t\t\t\"domain\": \"4c112a65-e6f2-4b0d-bfef-0912748bdb76\",\n" +
                "\t\t\t\"modifiedby\": \"GEO TESTING\",\n" +
                "\t\t\t\"modifiedon\": \"2016-07-24 00:00:00\",\n" +
                "\t\t\t\"tripid\": \"c244424c-f77a-4bd9-bf73-14c37b662009\",\n" +
                "\t\t\t\"bookcode\": \"V0F85RC\",\n" +
                "\t\t\t\"phonenum\": \"1234\",\n" +
                "\t\t\t\"stasiunidorg\": \"326a1c93-97ce-e1d9-e053-0b0610ac5298\",\n" +
                "\t\t\t\"stasiuniddest\": \"326a1c93-97ce-e1d9-e053-0b0610ac5502\",\n" +
                "\t\t\t\"subclassid\": \"b25bdd33-e4cd-4f04-a393-af11e70d6b4f\",\n" +
                "\t\t\t\"useridbook\": \"c199ef09-2d04-434e-92db-874d25451411\",\n" +
                "\t\t\t\"unitidbook\": \"91241cc2-1848-40f9-9833-1258ff0bedf1\",\n" +
                "\t\t\t\"shiftid\": \"eed4d4e9-f274-41f5-a4ce-a32f8ad34a25\",\n" +
                "\t\t\t\"totpsgadult\": 2,\n" +
                "\t\t\t\"totpsgchild\": 0,\n" +
                "\t\t\t\"totpsginfant\": 0,\n" +
                "\t\t\t\"createdon\": \"2016-07-24 00:00:00\",\n" +
                "\t\t\t\"booktimeouton\": \"2016-07-24 00:00:00\",\n" +
                "\t\t\t\"paytimeouton\": \"2016-10-15 00:00:00\",\n" +
                "\t\t\t\"paidon\": \"2016-09-29 10:41:49\",\n" +
                "\t\t\t\"scheduleid\": \"1c21110f-87cd-4997-8284-84084c60235d\",\n" +
                "\t\t\t\"noka\": \"99\",\n" +
                "\t\t\t\"trainname\": \"MALABAR\",\n" +
                "\t\t\t\"channelid\": \"6a242b7d-afe3-472d-8130-46ace3aad91f\",\n" +
                "\t\t\t\"extrafee\": 7500,\n" +
                "\t\t\t\"paidamount\": 0,\n" +
                "\t\t\t\"reroutestat\": \"0\",\n" +
                "\t\t\t\"localstat\": \"1\"\n" +
                "\t\t}]\n" +
                "\t},\n" +
                "\t\"status\": \"00\",\n" +
                "\t\"message\": \"SUCCESS\"\n" +
                "}";


        MessageWrapper<Object> messageWrapper = new MessageWrapper<>();
        List<Transaction> transactionList = messageWrapper.getValue(jsondata,"transactionlist",List.class);

    }
}
