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
            Map<byte[], Date> inactiveDevices = getInactiveDevices();
            if (inactiveDevices.isEmpty()) {
                dataStream.writeShort(0);
            }
            for (Entry<byte[], Date> entry : inactiveDevices.entrySet()) {
                int time = (int) (entry.getValue().getTime() / 1000L);
                dataStream.writeInt(time);
                byte[] bytes = entry.getKey();
                dataStream.writeShort(bytes.length);
                dataStream.write(bytes);
            }
            dataStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Map<byte[], Date> getInactiveDevices() {
        Map<byte[], Date> inactiveDevices = new HashMap<>();
        Date now = new Date();
        synchronized (devices) {
            for (Iterator<Device> iter = devices.iterator(); iter.hasNext();) {
                Device device = iter.next();
                Date date = device.getDate();
                if (now.getTime() - date.getTime() > 1000 * 60) {
                    inactiveDevices.put(device.getToken(), date);
                    iter.remove();
                }
            }
        }
        return inactiveDevices;
    }

}
