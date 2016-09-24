package ro.happyhyppo.apns;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notnoop.apns.internal.Utilities;

public abstract class AbstractService implements Runnable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    int portNumber;

    SSLServerSocketFactory socketFactory;

    static Map<String, Date> devices = new HashMap<String, Date>();

    public AbstractService(int portNumber) {
        this.portNumber = portNumber;
        socketFactory = serverContext().getServerSocketFactory();
    }

    SSLContext serverContext() {
        try {
            InputStream stream = AbstractService.class.getResourceAsStream("/keys/GenefoPushDev.p12");
            assert stream != null;
            return Utilities.newSSLContext(stream, "genefo", "PKCS12", "sunx509");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        log.info("Listening on port " + portNumber);
        try {
            ServerSocket serverSocket = socketFactory.createServerSocket((portNumber));
            while (true) {
                final Socket socket = serverSocket.accept();
                new Thread() {
                    public void run() {
                        log.debug("Incoming connection " + socket);
                        process(socket);
                    }
                }.start();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    abstract void process(Socket socket);

}
