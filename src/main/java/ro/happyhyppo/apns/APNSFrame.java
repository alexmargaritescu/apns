package ro.happyhyppo.apns;

public class APNSFrame {

    private byte[] identifier;

    private byte[] expiry;

    private byte[] token;

    private String payload;

    public APNSFrame(byte[] identifier, byte[] expiry, byte[] token, String payload) {
        super();
        this.identifier = identifier;
        this.expiry = expiry;
        this.token = token;
        this.payload = payload;
    }

    public byte[] getIdentifier() {
        return identifier;
    }

    public byte[] getExpiry() {
        return expiry;
    }

    public byte[] getToken() {
        return token;
    }

    public String getPayload() {
        return payload;
    }

}
