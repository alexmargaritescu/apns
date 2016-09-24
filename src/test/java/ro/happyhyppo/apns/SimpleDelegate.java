package ro.happyhyppo.apns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.DeliveryError;

public class SimpleDelegate implements ApnsDelegate {

    private final Logger log = LoggerFactory.getLogger(SimpleDelegate.class);

    @Override
    public void messageSent(ApnsNotification message, boolean resent) {
        log.debug("Message sent: " + message + " resent=" + resent);
    }

    @Override
    public void messageSendFailed(ApnsNotification message, Throwable e) {
        log.error("Message falied: " + message + " error=" + e.getMessage());
    }

    @Override
    public void connectionClosed(DeliveryError e, int messageIdentifier) {
        log.error("Connection closed: error=" + e + " message identifier=" + messageIdentifier);
    }

    @Override
    public void cacheLengthExceeded(int newCacheLength) {
        log.warn("Cache length exceeded; new cached length: " + newCacheLength);
    }

    @Override
    public void notificationsResent(int resendCount) {
        log.warn("Notifications resent: " + resendCount);
    }

}
