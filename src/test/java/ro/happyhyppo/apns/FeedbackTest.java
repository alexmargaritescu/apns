package ro.happyhyppo.apns;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notnoop.apns.ApnsService;

public class FeedbackTest extends AbstractTest {

    private final Logger log = LoggerFactory.getLogger(FeedbackTest.class);

    public static void main(String[] args) {
        try {
            new FeedbackTest().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    void test(ApnsService apnsService) throws Exception {
        Map<String, Date> inactiveDevices = apnsService.getInactiveDevices();
        log.debug("Inactive devices: " + inactiveDevices);
    }
}
