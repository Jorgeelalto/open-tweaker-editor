package app.com;

import javax.sound.midi.*;
import java.util.Arrays;
import java.util.List;


public class TweakerHandler {

    // RGB LED color constants
    public static final int OFF = 0;
    public static final int GREEN = 1;
    public static final int RED = 4;
    public static final int YELLOW = 8;
    public static final int BLUE = 16;
    public static final int CYAN = 32;
    public static final int MAGENTA = 64;
    public static final int WHITE = 127;
    public static final List<Integer> COLORS = Arrays.asList(GREEN, RED, YELLOW, BLUE, CYAN, MAGENTA, WHITE);



    private MidiDevice device;
    private Receiver receiver;


    public TweakerHandler() {
        // Set some state variables
        device = null;
        receiver = null;
    }


    public boolean isConnected() {
        return (device != null);
    }

    // Opens the device given a specific info. Used in automaticOpen and manualOpen
    private void openDevice(MidiDevice.Info info) throws Exception {

        // If the device is already open, try to close it beforehand
        if (device != null && device.isOpen()) device.close();
        // Get the MIDIDevice and open it
        device = MidiSystem.getMidiDevice(info);
        device.open();

        // If there is already a receiver around, close it
        if (receiver != null) receiver.close();
        // Open the receiver of the device
        receiver = device.getReceiver();
    }


    // Finds a Tweaker MIDI device and opens it. Returns true if successful operation
    public void automaticOpen() throws Exception {

        System.out.println("automaticOpen: Trying to autoconnect");
        // Get the list of MIDI devices
        MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
        // The device ID will be stored here
        MidiDevice.Info deviceInfo = null;

        // Try to find the device automatically
        for (MidiDevice.Info i : midiDeviceInfo) {
            System.out.println("name=" + i.getName() + " desc=" + i.getDescription());

            // Windows
            if (i.getName().equals("Tweaker") && i.getDescription().equals("External MIDI Port")) {
                // Set the deviceInfo to the one we found
                deviceInfo = i;
                break;
            }

            // macOS
            if (i.getDescription().contains("Tweaker")) {
                deviceInfo = i;
                break;
            }
        }

        if (deviceInfo == null) {
            System.out.println("automaticOpen: Tweaker MIDI hardware not found");
            throw new Exception("Tweaker hardware not found");
        }

        openDevice(deviceInfo);
        System.out.println("automaticOpen: Connected successfully");
    }


    // Opens the MIDI device with the ID specified in the parameters. The ID is an integer, index
    // of its corresponding deviceInfo in the list returned by MidiSystem.getMidiDeviceInfo().
    public void manualOpen(int id) throws Exception {

        // Get the list of MIDI devices
        MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();

        // Check whether the argument makes sense
        if (id < 0 || id > (midiDeviceInfo.length - 1)) {
            System.out.println("manualOpen: Argument id is invalid -- negative or too high");
        } else {
            // Store the device info
            MidiDevice.Info deviceInfo = midiDeviceInfo[id];
            openDevice(deviceInfo);
        }
    }


    // Send note message to device
    public void sendNote(int channel, int note, int velocity) {
        System.out.println("sendNote: ch: " + channel + ", cc: " + note + ", bb: " + velocity);

        // Internally the channel number has a range of 0 to 15 instead of the human readable 1 to 16
        channel--;
        // Check for arguments correctness. Our operational range for the byte data type is 0 to 127
        if (channel < 0 || channel > 15) {
            System.out.println("sendNote: channel argument is out of operational range (0 - 15)");
            return;
        }
        if (note < 0) {
            System.out.println("sendNote: note argument is out of operational range (0 - 127)");
            return;
        }
        if (velocity < 0) {
            System.out.println("sendNote: velocity argument is out of operational range (0 - 127)");
            return;
        }

        // Check the status of the device and receiver objects
        if (device == null || receiver == null) {
            System.out.println("sendNote: The device is not initialized -- calling automaticOpen should solve this");
            return;
        }

        ShortMessage message = new ShortMessage();
        // IMPORTANT STUFF
        // - The channel goes from 0 to 15 instead of 1 to 16
        // - In the case of ShortMessage.CONTROL_CHANGEs, data1 is CC number and data2 is velocity
        try {
            message.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
        } catch (Exception e) {
            System.out.println("sendNote: Could not set CC message data");
            e.printStackTrace();
            return;
        }

        receiver.send(message, -1);
    }


    // Send CC message to device
    public void sendCC(int channel, int cc, int velocity) {
        System.out.println("sendCC: ch: " + channel + ", cc: " + cc + ", bb: " + velocity);

        // Internally the channel number has a range of 0 to 15 instead of the human readable 1 to 16
        channel--;
        // Check for arguments correctness. Our operational range for the byte data type is 0 to 127
        if (channel < 0 || channel > 15) {
            System.out.println("sendCC: channel argument is out of operational range (0 - 15)");
            return;
        }
        if (cc < 0 || cc > 127) {
            System.out.println("sendCC: note argument is out of operational range (0 - 127)");
            return;
        }
        if (velocity < 0 || velocity > 127) {
            System.out.println("sendCC: velocity argument is out of operational range (0 - 127)");
            return;
        }

        // Check the status of the device and receiver objects
        if (device == null || receiver == null) {
            System.out.println("sendCC: The device is not initialized -- calling automaticOpen should solve this");
            return;
        }

        ShortMessage message = new ShortMessage();
        // IMPORTANT STUFF
        // - The channel goes from 0 to 15 instead of 1 to 16
        // - In the case of ShortMessage.CONTROL_CHANGEs, data1 is CC number and data2 is velocity
        try {
            message.setMessage(ShortMessage.CONTROL_CHANGE, channel, cc, velocity);
        } catch (Exception e) {
            System.out.println("sendCC: Could not set CC message data");
            e.printStackTrace();
            return;
        }

        receiver.send(message, -1);

    }


    // Send SysEx message to device
    public void sendSysEx(byte[] message) throws Exception {

        // Check the status of the device and receiver objects
        if (device == null || receiver == null) {
            System.out.println("sendCC: The device is not initialized -- calling automaticOpen should solve this");
            throw new Exception("sendSysEx: Device not initialized");
        }

        // Create the message object from the array
        SysexMessage sysexMessage = new SysexMessage();
        sysexMessage.setMessage(message, message.length);

        receiver.send(sysexMessage, -1);
    }


    // Light show
    public void lightShow() {

        new Thread(() -> {

            final int ms = 50;
            final int a2 = YELLOW;
            final int b2 = BLUE;

            int[] pos1 = {1, 10, 19, 28, 21, 14, 7, 16, 23, 30, 21, 12, 3, 10, 17};
            int[] pos2 = {32, 23, 14, 5, 12, 19, 26, 17, 10, 3, 12, 21, 30, 23, 16};

            for (int i = 0; i < pos1.length; i++) {

                sendNote(1, pos1[i], RED);
                if (i > 0) sendNote(1, pos1[i - 1], a2);
                if (i > 1) sendNote(1, pos1[i - 2], OFF);

                sendNote(1, pos2[i], MAGENTA);
                if (i > 0) sendNote(1, pos2[i - 1], b2);
                if (i > 1) sendNote(1, pos2[i - 2], OFF);

                try {
                    Thread.sleep(ms);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }

            sendNote(1, pos1[pos1.length - 2], OFF);
            sendNote(1, pos2[pos2.length - 2], OFF);
            sendNote(1, pos1[pos1.length - 1], a2);
            sendNote(1, pos2[pos2.length - 1], b2);
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            sendNote(1, pos1[pos1.length - 1], OFF);
            sendNote(1, pos2[pos2.length - 1], OFF);
        }).start();
    }
}
