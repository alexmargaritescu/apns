package ro.happyhyppo.apns;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APNSProtocol {

    private final Logger log = LoggerFactory.getLogger(APNSProtocol.class);

    private APNSConnection connection;

    private InputStream stream;

    public APNSProtocol(APNSConnection connection, InputStream stream) {
        super();
        this.connection = connection;
        this.stream = stream;
    }

    public void read() throws IOException {
        while (true) {
            if (!readCommand()) {
                break;
            }
            log.debug("Reading frame");
            byte[] identifier = readIdentifier();
            byte[] expiry = readExpiry();
            byte[] token = readToken();
            String payload = readPayload();
            APNSFrame frame = new APNSFrame(identifier, expiry, token, payload);
            if (!connection.processRequest(frame)) {
                break;
            };
        }
    }

    private boolean readCommand() throws IOException {
        int command = stream.read();
        if (command < 0) {
            return false;
        } else {
            return true;
        }
    }

    private byte[] readIdentifier() throws IOException {
        return readData(4, "identifier");
    }

    private byte[] readExpiry() throws IOException {
        return readData(4, "expiry");
    }

    private byte[] readToken() throws IOException {
        byte[] data = readData(2, "tokenLength");
        int tokenLength = java.nio.ByteBuffer.wrap(new byte[] { (byte) 0, (byte) 0, data[0], data[1] }).getInt();
        return readData(tokenLength, "token");
    }

    private String readPayload() throws IOException {
        byte[] data = readData(2, "payloadLength");
        int payloadLength = java.nio.ByteBuffer.wrap(new byte[] { (byte) 0, (byte) 0, data[0], data[1] }).getInt();
        data = readData(payloadLength, "payload");
        return new String(data);
    }

    private byte[] readData(int size, String name) throws IOException {
        byte[] data = new byte[size];
        size = stream.read(data);
        if (size < 0) {
            throw new IOException("No " + name + " data in stream");
        }
        return data;
    }

}
