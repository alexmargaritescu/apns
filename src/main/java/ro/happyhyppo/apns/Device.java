package ro.happyhyppo.apns;

import java.util.Arrays;
import java.util.Date;

public class Device {

    private byte[] token;

    private Date date;

    public Device(byte[] token) {
        super();
        this.token = token;
        this.date = new Date();
    }

    public byte[] getToken() {
        return token;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(token);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Device other = (Device) obj;
        if (!Arrays.equals(token, other.token))
            return false;
        return true;
    }

}
