package ro.happyhyppo.apns;

import java.io.InputStream;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.LoggerFactory;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.internal.Utilities;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public abstract class AbstractTest {

    public AbstractTest() {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.DEBUG);
    }

    void execute() throws Exception {
        String certificate = "keys/GenefoPushDev.p12";
        InputStream certStream = this.getClass().getClassLoader().getResourceAsStream(certificate);
        ApnsService apnsService = APNS.newService().withSSLContext(clientContext(certStream, "genefo"))
				.withGatewayDestination("localhost", 7779).withFeedbackDestination("localhost", 7778)
				.withDelegate(new SimpleDelegate()).build();
        apnsService.start();
        test(apnsService);
        apnsService.stop();
    }

    abstract void test(ApnsService apnsService) throws Exception;

    private SSLContext clientContext(InputStream certStream, String passwd) {
        try {
            SSLContext context = Utilities.newSSLContext(certStream, passwd, "PKCS12", "sunx509");
            context.init(null, new TrustManager[] { new X509TrustManagerTrustAll() }, new SecureRandom());
            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class X509TrustManagerTrustAll implements X509TrustManager {
        public boolean checkClientTrusted(java.security.cert.X509Certificate[] chain) {
            return true;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] chain) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] chain) {
            return true;
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }
    }
}
