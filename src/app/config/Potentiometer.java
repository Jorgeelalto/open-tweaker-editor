package app.config;

class Potentiometer {
    // 0 to 127
    private byte mapping;
    // 1 to 16
    private byte channel;


    public Potentiometer() {
        mapping = 0;
        channel = 1;
    }


    public byte getMapping() {
        return mapping;
    }

    public void setMapping(byte mapping) throws IllegalArgumentException {
        if (mapping < 0) {
            throw new IllegalArgumentException();
        } else {
            this.mapping = mapping;
        }
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) throws IllegalArgumentException {
        if (channel < 1 || channel > 16) {
            throw new IllegalArgumentException();
        } else {
            this.channel = channel;
        }
    }
}
