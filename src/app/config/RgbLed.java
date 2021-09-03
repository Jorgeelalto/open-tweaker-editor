package app.config;

import java.util.Arrays;

// Lights are linked to their corresponding control (encoder,
// button, pad) according to page 13 in the Tweaker User Manual
class RgbLed {
    // (o)ff, (g)reen, (r)ed, (y)ellow, (b)lue, (c)yan, (m)agenta, (w)hite
    private char color;
    // Mapping
    private byte mapping;
    // Channel
    private byte channel;

    public RgbLed() {
        color = 'o';
        mapping = 0;
        channel = 1;
    }


    public char getColor() {
        return color;
    }

    public void setColor(char color) throws IllegalArgumentException {
        if (Arrays.asList('o', 'g', 'r', 'y', 'b', 'c', 'm', 'w').contains(color)) {
            this.color = color;
        } else {
            throw new IllegalArgumentException();
        }
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
