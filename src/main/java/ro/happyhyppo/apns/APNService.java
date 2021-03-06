package ro.happyhyppo.apns;

import java.net.Socket;

public class APNService extends AbstractService {

    public APNService(int portNumber) {
        super(portNumber);
    }

    @Override
    void process(Socket socket) {
        new APNSConnection(this, socket).process();
    }

    void addToken(byte[] token) {
        synchronized (devices) {
            devices.add(new Device(token));
        }
    }

}
