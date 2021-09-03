package app.config;

class Button {
    // 0 to 127
    private byte mapping;
    // 1 to 16
    private byte channel;
    // false = CC, true = Note;
    private boolean outputType;
    // false = disabled, true = enabled
    private boolean speedControl;
    // false = disabled, true = enabled
    private boolean localControl;


    public Button() {
        mapping = 0;
        channel = 1;
        outputType = true;
        speedControl = false;
        localControl = false;
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

    public boolean getOutputType() {
        return outputType;
    }

    public void setOutputType(boolean outputType) {
        this.outputType = outputType;
    }

    public boolean getSpeedControl() {
        return speedControl;
    }

    public void setSpeedControl(boolean speedControl) {
        this.speedControl = speedControl;
    }

    public boolean getLocalControl() {
        return localControl;
    }

    public void setLocalControl(boolean localControl) {
        this.localControl = localControl;
    }
}
