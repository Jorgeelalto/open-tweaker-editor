package app.config;

class Pad {
    // 0 to 127
    private byte hitMapping;
    // 1 to 16
    private byte hitChannel;
    // 0 to 127
    private byte retriggerMapping;
    // 1 to 16
    private byte retriggerChannel;
    // False = disabled, True = enabled
    private boolean ccRetrigger17;
    // False = disabled, True = enabled
    private boolean ccRetrigger8;
    // 0 to 127?
    private byte onThresholdLow;
    // 0 to 127?
    private byte onThresholdHigh;
    // 0 to 127?
    private byte offThresholdLow;
    // 0 to 127?
    private byte offThresholdHigh;
    // 1 to ??
    private byte resendRate;
    // 0 to 5
    private byte sensitivity;


    public Pad() {
        hitMapping = 0;
        hitChannel = 1;
        retriggerMapping = 1;
        retriggerChannel = 1;
        ccRetrigger17 = true;
        ccRetrigger8 = true;
        onThresholdLow = 15;
        onThresholdHigh = 0;
        offThresholdLow = 7;
        offThresholdHigh = 0;
        resendRate = 9;
        sensitivity = 5;
    }


    public byte getHitMapping() {
        return hitMapping;
    }

    public void setHitMapping(byte hitMapping) throws IllegalArgumentException {
        if (hitMapping < 0) {
            throw new IllegalArgumentException();
        } else {
            this.hitMapping = hitMapping;
        }
    }

    public byte getHitChannel() {
        return hitChannel;
    }

    public void setHitChannel(byte hitChannel) throws IllegalArgumentException {
        if (hitChannel < 1 || hitChannel > 16) {
            throw new IllegalArgumentException();
        } else {
            this.hitChannel = hitChannel;
        }
    }

    public byte getRetriggerMapping() {
        return retriggerMapping;
    }

    public void setRetriggerMapping(byte retriggerMapping) throws IllegalArgumentException {
        if (retriggerMapping < 0) {
            throw new IllegalArgumentException();
        } else {
            this.retriggerMapping = retriggerMapping;
        }
    }

    public byte getRetriggerChannel() {
        return retriggerChannel;
    }

    public void setRetriggerChannel(byte retriggerChannel) throws IllegalArgumentException {
        if (retriggerChannel < 1 || retriggerChannel > 16) {
            throw new IllegalArgumentException();
        } else {
            this.retriggerChannel = retriggerChannel;
        }
    }

    public boolean getCcRetrigger17() {
        return ccRetrigger17;
    }

    public void setCcRetrigger17(boolean ccRetrigger17) {
        this.ccRetrigger17 = ccRetrigger17;
    }

    public boolean getCcRetrigger8() {
        return ccRetrigger8;
    }

    public void setCcRetrigger8(boolean ccRetrigger8) {
        this.ccRetrigger8 = ccRetrigger8;
    }

    public byte getOnThresholdLow() {
        return onThresholdLow;
    }

    public void setOnThresholdLow(byte onThresholdLow) throws IllegalArgumentException {
        if (onThresholdLow < 0) {
            throw new IllegalArgumentException();
        } else {
            this.onThresholdLow = onThresholdLow;
        }
    }

    public byte getOnThresholdHigh() {
        return onThresholdHigh;
    }

    public void setOnThresholdHigh(byte onThresholdHigh) throws IllegalArgumentException {
        if (onThresholdHigh < 0) {
            throw new IllegalArgumentException();
        } else {
            this.onThresholdHigh = onThresholdHigh;
        }
    }

    public byte getOffThresholdLow() {
        return offThresholdLow;
    }

    public void setOffThresholdLow(byte offThresholdLow) throws IllegalArgumentException {
        if (offThresholdLow < 0) {
            throw new IllegalArgumentException();
        } else {
            this.offThresholdLow = offThresholdLow;
        }
    }

    public byte getOffThresholdHigh() {
        return offThresholdHigh;
    }

    public void setOffThresholdHigh(byte offThresholdHigh) throws IllegalArgumentException {
        if (offThresholdHigh < 0) {
            throw new IllegalArgumentException();
        } else {
            this.offThresholdHigh = offThresholdHigh;
        }
    }

    public byte getResendRate() {
        return resendRate;
    }

    public void setResendRate(byte resendRate) throws IllegalArgumentException {
        // TODO check bounds
        if (resendRate < 1 || resendRate > 15) {
            throw new IllegalArgumentException();
        } else {
            this.resendRate = resendRate;
        }
    }

    public byte getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(byte sensitivity) throws IllegalArgumentException {
        if (sensitivity < 0 || sensitivity > 5) {
            throw new IllegalArgumentException();
        } else {
            this.sensitivity = sensitivity;
        }
    }
}
