package ro.happyhyppo.apns;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedbackService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(FeedbackService.class);

    public FeedbackService(int portNumber) {
        super(portNumber);
    }

    @Override
    void process(Socket socket) {
        try {
            DataOutputStream dataStream = new DataOutputStream(socket.getOutputStream());
            Map<String, Date> inactiveDevices = getInactiveDevices();
            if (inactiveDevices.isEmpty()) {
                dataStream.writeShort(0);
            }
            for (Entry<String, Date> entry : inactiveDevices.entrySet()) {
                int time = (int) (entry.getValue().getTime() / 1000L);
                dataStream.writeInt(time);
                byte[] bytes = entry.getKey().getBytes();
                dataStream.writeShort(bytes.length);
                dataStream.write(bytes);
            }
            dataStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, Date> getInactiveDevices() {
        Map<String, Date> inactiveDevices = new HashMap<>();
        Date now = new Date();
        synchronized (devices) {
            for (Iterator<String> iter = devices.keySet().iterator(); iter.hasNext();) {
                String token = iter.next();
                Date date = devices.get(token);
                if (now.getTime() - date.getTime() > 1000 * 60) {
                    inactiveDevices.put(token, date);
                    iter.remove();
                }
            }
        }
        return inactiveDevices;
    }

}
