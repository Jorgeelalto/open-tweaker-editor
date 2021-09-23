package app.config;

import java.util.Arrays;

class Encoder {
    // (f)ill, (w)alk, (e)q, (s)pread
    private char ringMode;
    // false = absolute, true = relative
    private boolean relativeMode;
    // 1 to 7
    private byte speed;
    // False = disabled, True = enabled
    private boolean localControl;
    // 0 to 127
    private byte mapping;
    // 1 to 16
    private byte channel;
    // 0 to 127
    private byte ledMapping;
    // 1 to 16
    private byte ledChannel;


    public Encoder() {
        ringMode = 'e';
        relativeMode = false;
        speed = 5;
        localControl = true;
        mapping = 0;
        channel = 1;
        ledMapping = 0;
        ledChannel = 1;
    }

    public char getRingMode() {
        return ringMode;
    }

    public void setRingMode(char ringMode) throws IllegalArgumentException {
        if (Arrays.asList('f', 'w', 'e', 's').contains(ringMode)) {
            this.ringMode = ringMode;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean getRelativeMode() {
        return relativeMode;
    }

    public void setRelativeMode(boolean relativeMode) {
        this.relativeMode = relativeMode;
    }

    public byte getSpeed() {
        return speed;
    }

    public void setSpeed(byte speed) throws IllegalArgumentException {
        if (speed < 1 || speed > 7) {
            throw new IllegalArgumentException();
        } else {
            this.speed = speed;
        }
    }

    public boolean getLocalControl() {
        return localControl;
    }

    public void setLocalControl(boolean localControl) {
        this.localControl = localControl;
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

    /*public byte getLedMapping() {
        return ledMapping;
    }*/

    public void setLedMapping(byte ledMapping) throws IllegalArgumentException {
        if (ledMapping < 0) {
            throw new IllegalArgumentException();
        } else {
            this.ledMapping = ledMapping;
        }
    }

    public byte getLedChannel() {
        return ledChannel;
    }

    public void setLedChannel(byte ledChannel) throws IllegalArgumentException {
        if (ledChannel < 1 || ledChannel > 16) {
            throw new IllegalArgumentException();
        } else {
            this.ledChannel = ledChannel;
        }
    }
}
