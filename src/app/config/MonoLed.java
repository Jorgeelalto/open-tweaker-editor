package app.config;

public class MonoLed {
    // true = On, false = Off
    private boolean status;
    // Mapping
    private byte mapping;
    // Channel
    private byte channel;

    public MonoLed() {
        status = false;
        mapping = 0;
        channel = 1;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public byte getMapping() {
        return mapping;
    }

    public void setMapping(byte mapping) {
        this.mapping = mapping;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }
}
