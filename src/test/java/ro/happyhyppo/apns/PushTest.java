package ro.happyhyppo.apns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;

public class PushTest extends AbstractTest {

    private final Logger log = LoggerFactory.getLogger(PushTest.class);

    public static void main(String[] args) {
        try {
            new PushTest().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    void test(ApnsService apnsService) throws Exception {
        PayloadBuilder payloadBuilder = APNS.newPayload();
        payloadBuilder = payloadBuilder.alertTitle("GeneFo");
        payloadBuilder = payloadBuilder.alertBody("Someone liked your comment!");
        String payload = payloadBuilder.build();
        List<String> tokens = new ArrayList<>();
        tokens.add("64656661756C745F7365676D656E746D697865645F696F735F34a01c12c8c177");
        tokens.add("64656661756C745F7365676D656E746D697865645F696F735F34a01c12c8c178");
        Collection<? extends ApnsNotification> result = apnsService.push(tokens, payload);
        log.debug("Messages sent: " + result);
        Thread.sleep(1000);
        log.debug("Test done!");
    }

}
