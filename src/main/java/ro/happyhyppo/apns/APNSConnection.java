package ro.happyhyppo.apns;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APNSConnection {

    private APNService service;

    private Socket socket;

    private InputStream in;

    private OutputStream out;

    public APNSConnection(APNService service, Socket socket) {
        this.service = service;
        this.socket = socket;
    }

    private final Logger log = LoggerFactory.getLogger(APNSConnection.class);

    public void process() {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            new APNSProtocol(this, in).read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    private boolean write(APNSFrame frame, OutputStream st) throws IOException {
    	DataOutputStream dataStream = new DataOutputStream(st);
    	if (hexFormat(frame.getToken()).equalsIgnoreCase("64656661756C745F7365676D656E746D697865645F696F735F34a01c12c8c177")) {
            dataStream.writeByte(8);
            dataStream.writeByte(8);
            dataStream.write(frame.getIdentifier());
            return false;
        }
    	return true;
    }

    boolean processRequest(APNSFrame frame) throws IOException {
        byte[] id = frame.getIdentifier();
        byte[] expiry = frame.getExpiry();
        byte[] token = frame.getToken();
        String payload = frame.getPayload();
        log.debug("Identifier: " + java.nio.ByteBuffer.wrap(new byte[] { id[0], id[1], id[2], id[3] }).getInt());
        log.debug("Expiry: "
                + java.nio.ByteBuffer.wrap(new byte[] { expiry[0], expiry[1], expiry[2], expiry[3] }).getInt());
        log.debug("Token: " + hexFormat(token));
        log.debug("payload: " + prettyFormat(payload));
        service.addToken(token);
        return write(frame, out);
    }

    private String hexFormat(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            builder.append(String.format("%02x", data[i]));
        }
        return builder.toString();
    }

    private String prettyFormat(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readValue(json, Object.class));
        } catch (IOException e) {
            return json;
        }
    }
}
