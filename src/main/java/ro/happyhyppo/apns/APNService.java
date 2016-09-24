package ro.happyhyppo.apns;

import java.net.Socket;
import java.util.Date;

public class APNService extends AbstractService {

    public APNService(int portNumber) {
        super(portNumber);
    }

    @Override
    void process(Socket socket) {
        new APNSConnection(this, socket).process();
    }

    void addToken(String token) {
        synchronized (devices) {
            devices.put(new String(token), new Date());
        }
    }

}
