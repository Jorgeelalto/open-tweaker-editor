package app.config;

import app.com.TweakerHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class TweakerConfig {

    // Application control fields
    private boolean synced;
    private final TweakerHandler handler;

    // Controls (check controls.svg)
    // Encoders (in blue)
    private Encoder[] encoders = new Encoder[7];
    // Potentiometers (in green)
    private final Potentiometer[] potentiometers =  new Potentiometer[5];
    // Buttons (in yellow)
    private final Button[] buttons = new Button[50];
    // Pads (in red)
    private final Pad[] pads = new Pad[8];

    // Lights
    private final RgbLed[] butLeds = new RgbLed[38];
    //private final MonoLed[] padLeds = new MonoLed[8];
    private final MonoLed[] navLeds = new MonoLed[5];
    //private final MonoLed[] encLeds = new MonoLed[6];



    private void printByteArray(byte[] array) {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            if (i == array.length - 1) s.append(array[i] & 0xFF).append("]");
            else s.append(array[i] & 0xFF).append(", ");
        }
        System.out.println(s);
    }


    // Initialize handler and control configurations
    public TweakerConfig() {

        // This will only be true immediately after dumping the config to the hardware,
        // and will become false when a change is made to the config
        synced = false;
        handler = new TweakerHandler();

        // Set values for all the controls
        // - Encoders
        for (int i = 0; i < encoders.length; i++) {
            encoders[i] = new Encoder();
            // Change the mapping and LED mapping
            encoders[i].setMapping((byte) (56 + i));
            encoders[i].setLedMapping((byte) (79 + i));
        }
        // - Potentiometers
        for (int i = 0; i < potentiometers.length; i++) {
            potentiometers[i] = new Potentiometer();
            potentiometers[i].setMapping((byte) (51 + i));
        }
        // - Buttons
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button();
            buttons[i].setMapping((byte) (1 + i));
        }
        // - Pads
        for (int i = 0; i < pads.length; i++) {
            pads[i] = new Pad();
            pads[i].setHitMapping((byte) (63 + i));
            pads[i].setRetriggerMapping((byte) (71 + i));
        }
        // - Button RGB Lights
        for (int i = 0; i < butLeds.length; i++) {
            butLeds[i] = new RgbLed();
            butLeds[i].setMapping((byte) (i + 1));
        }
        // - Pad lights
        /*for (int i = 0; i < padLeds.length; i++) {
            padLeds[i] = new MonoLed();
            padLeds[i].setMapping((byte) (i + 63));
        }*/
        // - Navigation lights
        for (int i = 0; i < navLeds.length; i++) {
            navLeds[i] = new MonoLed();
            navLeds[i].setMapping((byte) (i + 39));
        }
        // - Encoder lights
        /*for (int i = 0; i < encLeds.length; i++) {
            encLeds[i] = new MonoLed();
            encLeds[i].setMapping((byte) (i + 44));
        }*/

        // Try to connect to the hardware
        try {
            handler.automaticOpen();
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.WARNING, "The Electrix Tweaker is not connected. " +
                    "Dumping will not work until it is.");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.showAndWait();
        }
    }


    // Dump the configuration to the hardware
    public void dump() {

        // Check if the hardware is connected, otherwise do not dump
        if (!handler.isConnected()) {
            Alert a = new Alert(Alert.AlertType.ERROR, "The Electrix Tweaker is not connected. " +
                    "Please connect the hardware and press OK to try again.");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.showAndWait();
            try {
                handler.automaticOpen();
            } catch (Exception e) {
                a = new Alert(Alert.AlertType.ERROR, "The connection could not be established " +
                        "(" + e.getMessage() + "). Nothing will be dumped.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            }
        }

        // Compose the SysEx byte arrays
        byte[] a;


        // -+- Button mappings -+-
        a = new byte[107];
        // Headers
        a[0] = (byte) 240;
        a[1] = (byte) 0;
        a[2] = (byte) 1;
        a[3] = (byte) 106;
        a[4] = (byte) 1;
        a[5] = (byte) 1;
        a[106]=(byte) 247;
        // Data
        for (int i = 0; i < buttons.length; i++) {
            // Channel number / output type TODO because its not always zero
            a[6 + i * 2] = (byte) 0;
            // Mapping
            a[6 + i * 2 + 1] = buttons[i].getMapping();
        }
        // Send
        try {handler.sendSysEx(a);}
        catch (Exception e) {
            Alert al = new Alert(Alert.AlertType.ERROR, "Could not send button mappings SysEx " +
                    "(" + e.getMessage() + "). Nothing else will be dumped.");
            al.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            al.showAndWait();
            return;
        }

        // -+- Potentiometer mappings -+-
        a = new byte[17];
        // Headers
        a[0] = (byte) 240;
        a[1] = (byte) 0;
        a[2] = (byte) 1;
        a[3] = (byte) 106;
        a[4] = (byte) 1;
        a[5] = (byte) 2;
        a[16]= (byte) 247;
        // Data
        for (int i = 0; i < potentiometers.length; i++) {
            // Channel number / output type TODO because its not always zero
            a[6 + i * 2] = (byte) 0;
            // Mapping
            a[6 + i * 2 + 1] = potentiometers[i].getMapping();
        }
        // Send
        try {handler.sendSysEx(a);}
        catch (Exception e) {
            Alert al = new Alert(Alert.AlertType.ERROR, "Could not send potentiometer mappings SysEx " +
                    "(" + e.getMessage() + "). Nothing else will be dumped.");
            al.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            al.showAndWait();
            return;
        }


        // -+- Encoder mappings -+-
        a = new byte[21];
        // Headers
        a[0] = (byte) 240;
        a[1] = (byte) 0;
        a[2] = (byte) 1;
        a[3] = (byte) 106;
        a[4] = (byte) 1;
        a[5] = (byte) 3;
        a[20]= (byte) 247;
        // Data
        for (int i = 0; i < encoders.length; i++) {
            // Channel number / output type TODO because its not always zero
            a[6 + i * 2] = (byte) 0;
            // Mapping
            a[6 + i * 2 + 1] =  encoders[i].getMapping();
        }
        // Send
        try {handler.sendSysEx(a);}
        catch (Exception e) {
            Alert al = new Alert(Alert.AlertType.ERROR, "Could not send encoder mappings SysEx " +
                    "(" + e.getMessage() + "). Nothing else will be dumped.");
            al.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            al.showAndWait();
            return;
        }

        // -+- Pad mappings -+-
        a = new byte[39];
        // Headers
        a[0] = (byte) 240;
        a[1] = (byte) 0;
        a[2] = (byte) 1;
        a[3] = (byte) 106;
        a[4] = (byte) 1;
        a[5] = (byte) 4;
        a[38]= (byte) 247;
        // Data
        for (int i = 0; i < pads.length; i++) {
            //a[6 + i] =     pads[i].getHitChannel();
            a[6 + i * 2 + 1] = pads[i].getHitMapping();
        }
        for (int i = 0; i < pads.length; i++) {
            //a[6 + pads.length + i] =     pads[i].getRetriggerChannel();
            a[6 + (pads.length + i) * 2 + 1] = pads[i].getRetriggerMapping();
        }
        // Send
        printByteArray(a);
        try {handler.sendSysEx(a);}
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not send pad mappings SysEx " +
                    "(" + e.getMessage() + "). Nothing else will be dumped.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            return;
        }



        //handler.lightShow();
        // TODO should this be here?
        synced = true;
    }


    // TODO Load configuration from file
    public void loadFromFile(File file) {
        System.out.println("loadFromFile: load from " + file.getAbsolutePath());

        Reader reader;
        try {reader = new FileReader(file);}
        catch (Exception e) {
            System.out.println("loadFromFile: Could not read the file");
            Alert a = new Alert(Alert.AlertType.ERROR, "The file could not be opened or read (" + e.getMessage() + ").");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.showAndWait();
            return;
        }

        // Read the file into a Map object
        Gson gson = new GsonBuilder().create();
        Map<?, ?> map = gson.fromJson(reader, Map.class);

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        // Check the validity of the data
        // - Encoders
        ArrayList<LinkedTreeMap> encoderList = (ArrayList<LinkedTreeMap>) map.get("encoders");
        for (LinkedTreeMap<?, ?> enc : encoderList) {
            // Check every field
            // + ringMode
            if (!enc.containsKey("ringMode") || !(Arrays.asList("f", "w", "e", "s").contains(enc.get("ringMode")))) {
                System.out.println("loadFromFile verification: No ringMode or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'ringMode' from an encoder: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + relativeMode
            } else if (!enc.containsKey("relativeMode") || !(enc.get("relativeMode") instanceof Boolean)) {
                System.out.println("loadFromFile verification: No relativeMode or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'relativeMode' from an encoder: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + speed
            } else if (!enc.containsKey("speed") || ((Double) enc.get("speed") < 1 || (Double) enc.get("speed") > 7)) {
                System.out.println("loadFromFile verification: No speed or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'speed' from an encoder: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + localControl
            } else if (!enc.containsKey("localControl") || !(enc.get("relativeMode") instanceof Boolean)) {
                System.out.println("loadFromFile verification: No localControl or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'localControl' from an encoder: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + mapping
            } else if (!enc.containsKey("mapping") || ((Double) enc.get("mapping") < 0 || (Double) enc.get("mapping") > 127)) {
                System.out.println("loadFromFile verification: No mapping or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'mapping' from an encoder: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + channel
            } else if (!enc.containsKey("channel") || ((Double) enc.get("channel") < 1 || (Double) enc.get("channel") > 16)) {
                System.out.println("loadFromFile verification: No channel or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'channel' from an encoder: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + ledMapping
            } else if (!enc.containsKey("ledMapping") || ((Double) enc.get("ledMapping") < 0 || (Double) enc.get("ledMapping") > 127)) {
                System.out.println("loadFromFile verification: No ledMapping or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'ledMapping' from an encoder: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + ledChannel
            } else if (!enc.containsKey("ledChannel") || ((Double) enc.get("ledChannel") < 1 || (Double) enc.get("ledChannel") > 16)) {
                System.out.println("loadFromFile verification: No ledChannel or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'ledChannel' from an encoder: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            }
        }
        // - Potentiometers
        ArrayList<LinkedTreeMap> potentiometerList = (ArrayList<LinkedTreeMap>) map.get("potentiometers");
        for (LinkedTreeMap<?, ?> pot : potentiometerList) {
            // Check every field
            // + mapping
            if (!pot.containsKey("mapping") || ((Double) pot.get("mapping") < 0 || (Double) pot.get("mapping") > 127)) {
                System.out.println("loadFromFile verification: No mapping or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'mapping' from a potentiometer: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + channel
            } else if (!pot.containsKey("channel") || ((Double) pot.get("channel") < 1 || (Double) pot.get("channel") > 16)) {
                System.out.println("loadFromFile verification: No channel or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'channel' from an encoder: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            }
        }
        // - Buttons
        ArrayList<LinkedTreeMap> buttonList = (ArrayList<LinkedTreeMap>) map.get("buttons");
        for (LinkedTreeMap<?, ?> but : buttonList) {
            // Check every field
            // + mapping
            if (!but.containsKey("mapping") || ((Double) but.get("mapping") < 0 || (Double) but.get("mapping") > 127)) {
                System.out.println("loadFromFile verification: No mapping or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'mapping' from a button: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + channel
            } else if (!but.containsKey("channel") || ((Double) but.get("channel") < 1 || (Double) but.get("channel") > 16)) {
                System.out.println("loadFromFile verification: No channel or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'channel' from a button: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + outputType
            } else if (!but.containsKey("outputType") || !(but.get("outputType") instanceof Boolean)) {
                System.out.println("loadFromFile verification: No outputType or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'outputType' from a button: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + speedControl
            } else if (!but.containsKey("speedControl") || !(but.get("speedControl") instanceof Boolean)) {
                System.out.println("loadFromFile verification: No speedControl or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'speedControl' from a button: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + localControl
            } else if (!but.containsKey("localControl") || !(but.get("localControl") instanceof Boolean)) {
                System.out.println("loadFromFile verification: No localControl or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'localControl' from a button: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            }
        }
        // - Pads
        ArrayList<LinkedTreeMap> padList = (ArrayList<LinkedTreeMap>) map.get("pads");
        for (LinkedTreeMap<?, ?> pad : padList) {
            // + hitMapping
            if (!pad.containsKey("hitMapping") || ((Double) pad.get("hitMapping") < 0 || (Double) pad.get("hitMapping") > 127)) {
                System.out.println("loadFromFile verification: No hitMapping or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'hitMapping' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + hitChannel
            } else if (!pad.containsKey("hitChannel") || ((Double) pad.get("hitChannel") < 1 || (Double) pad.get("hitChannel") > 16)) {
                System.out.println("loadFromFile verification: No hitChannel or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'hitChannel' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + retriggerMapping
            }else if (!pad.containsKey("retriggerMapping") || ((Double) pad.get("retriggerMapping") < 0 || (Double) pad.get("retriggerMapping") > 127)) {
                System.out.println("loadFromFile verification: No hitMapping or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'retriggerMapping' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + retriggerChannel
            } else if (!pad.containsKey("retriggerChannel") || ((Double) pad.get("retriggerChannel") < 1 || (Double) pad.get("retriggerChannel") > 16)) {
                System.out.println("loadFromFile verification: No retriggerChannel or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'retriggerChannel' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + ccRetrigger17
            } else if (!pad.containsKey("ccRetrigger17") || !(pad.get("ccRetrigger17") instanceof Boolean)) {
                System.out.println("loadFromFile verification: No ccRetrigger17 or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'ccRetrigger17' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + ccRetrigger8
            } else if (!pad.containsKey("ccRetrigger8") || !(pad.get("ccRetrigger8") instanceof Boolean)) {
                System.out.println("loadFromFile verification: No ccRetrigger8 or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'ccRetrigger8' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + onThresholdLow
            } else if (!pad.containsKey("onThresholdLow") || ((Double) pad.get("onThresholdLow") < 0 || (Double) pad.get("onThresholdLow") > 127)) {
                System.out.println("loadFromFile verification: No onThresholdLow or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'onThresholdLow' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + onThresholdHigh
            } else if (!pad.containsKey("onThresholdHigh") || ((Double) pad.get("onThresholdHigh") < 0 || (Double) pad.get("onThresholdHigh") > 127)) {
                System.out.println("loadFromFile verification: No onThresholdHigh or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'onThresholdHigh' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + offThresholdLow
            } else if (!pad.containsKey("offThresholdLow") || ((Double) pad.get("offThresholdLow") < 0 || (Double) pad.get("offThresholdLow") > 127)) {
                System.out.println("loadFromFile verification: No offThresholdLow or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'offThresholdLow' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + offThresholdHigh
            } else if (!pad.containsKey("offThresholdHigh") || ((Double) pad.get("offThresholdHigh") < 0 || (Double) pad.get("offThresholdHigh") > 127)) {
                System.out.println("loadFromFile verification: No offThresholdHigh or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'offThresholdHigh' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + resendRate
            } else if (!pad.containsKey("resendRate") || ((Double) pad.get("resendRate") < 1 || (Double) pad.get("resendRate") > 15)) {
                System.out.println("loadFromFile verification: No resendRate or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'resendRate' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + sensitivity
            } else if (!pad.containsKey("sensitivity") || ((Double) pad.get("sensitivity") < 0 || (Double) pad.get("sensitivity") > 5)) {
                System.out.println("loadFromFile verification: No sensitivity or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'sensitivity' from a pad: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            }
        }
        // - ButLeds
        ArrayList<LinkedTreeMap> butLedList = (ArrayList<LinkedTreeMap>) map.get("butLeds");
        for (LinkedTreeMap<?, ?> led : butLedList) {
            // + color
            if (!led.containsKey("color") || !(Arrays.asList("o", "g", "r", "y", "b", "c", "m", "w").contains(led.get("color")))) {
                System.out.println("loadFromFile verification: No color or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'color' from a button led: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + mapping
            } else if (!led.containsKey("mapping") || ((Double) led.get("mapping") < 0 || (Double) led.get("mapping") > 127)) {
                System.out.println("loadFromFile verification: No mapping or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'mapping' from a button led: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            // + channel
            } else if (!led.containsKey("channel") || ((Double) led.get("channel") < 1 || (Double) led.get("channel") > 16)) {
                System.out.println("loadFromFile verification: No channel or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'channel' from a button led: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            }
        }
        // - Navleds
        ArrayList<LinkedTreeMap> navLedList = (ArrayList<LinkedTreeMap>) map.get("navLeds");
        for (LinkedTreeMap<?, ?> led : navLedList) {
            // + status
            if (!led.containsKey("status") || !(led.get("status") instanceof Boolean)) {
                System.out.println("loadFromFile verification: No status or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'status' from a navigation button led: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
                // + mapping
            } else if (!led.containsKey("mapping") || ((Double) led.get("mapping") < 0 || (Double) led.get("mapping") > 127)) {
                System.out.println("loadFromFile verification: No mapping or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'mapping' from a navigation button led: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
                // + channel
            } else if (!led.containsKey("channel") || ((Double) led.get("channel") < 1 || (Double) led.get("channel") > 16)) {
                System.out.println("loadFromFile verification: No channel or wrong data");
                Alert a = new Alert(Alert.AlertType.ERROR, "Could not verify field 'channel' from a navigation button led: " +
                        "The data does not exist or it has an invalid value. The file will not be loaded.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            }
        }


        System.out.println("loadFromFile: File verified successfully");

        // If everything is correct, copy the data to the configuration
        // - Encoders
        for (int i = 0; i < encoderList.size(); i++) {
            encoders[i].setRingMode(((String) encoderList.get(i).get("ringMode")).charAt(0));
            encoders[i].setRelativeMode((Boolean) encoderList.get(i).get("relativeMode"));
            encoders[i].setSpeed(((Double) encoderList.get(i).get("speed")).byteValue());
            encoders[i].setLocalControl((Boolean) encoderList.get(i).get("localControl"));
            encoders[i].setMapping(((Double) encoderList.get(i).get("mapping")).byteValue());
            encoders[i].setChannel(((Double) encoderList.get(i).get("channel")).byteValue());
            encoders[i].setLedMapping(((Double) encoderList.get(i).get("ledMapping")).byteValue());
            encoders[i].setLedChannel(((Double) encoderList.get(i).get("ledChannel")).byteValue());
        }
        // - Potentiometers
        for (int i = 0; i < potentiometerList.size(); i++) {
            potentiometers[i].setMapping(((Double) potentiometerList.get(i).get(("mapping"))).byteValue());
            potentiometers[i].setChannel(((Double) potentiometerList.get(i).get(("channel"))).byteValue());
        }
        // - Buttons
        for (int i = 0; i < buttonList.size(); i++) {
            buttons[i].setMapping(((Double) buttonList.get(i).get("mapping")).byteValue());
            buttons[i].setChannel(((Double) buttonList.get(i).get("channel")).byteValue());
            buttons[i].setOutputType((Boolean) buttonList.get(i).get("outputType"));
            buttons[i].setSpeedControl((Boolean) buttonList.get(i).get("speedControl"));
            buttons[i].setLocalControl((Boolean) buttonList.get(i).get("localControl"));
        }
        // - Pads
        for (int i = 0; i < padList.size(); i++) {
            pads[i].setHitMapping(((Double) padList.get(i).get("hitMapping")).byteValue());
            pads[i].setHitChannel(((Double) padList.get(i).get("hitChannel")).byteValue());
            pads[i].setRetriggerMapping(((Double) padList.get(i).get("retriggerMapping")).byteValue());
            pads[i].setRetriggerChannel(((Double) padList.get(i).get("retriggerChannel")).byteValue());
            pads[i].setCcRetrigger17((Boolean) padList.get(i).get("ccRetrigger17"));
            pads[i].setCcRetrigger8((Boolean) padList.get(i).get("ccRetrigger8"));
            pads[i].setOnThresholdLow(((Double) padList.get(i).get("onThresholdLow")).byteValue());
            pads[i].setOnThresholdHigh(((Double) padList.get(i).get("onThresholdHigh")).byteValue());
            pads[i].setOffThresholdLow(((Double) padList.get(i).get("offThresholdLow")).byteValue());
            pads[i].setOffThresholdHigh(((Double) padList.get(i).get("offThresholdHigh")).byteValue());
            pads[i].setResendRate(((Double) padList.get(i).get("resendRate")).byteValue());
            pads[i].setSensitivity(((Double) padList.get(i).get("sensitivity")).byteValue());
        }
        // - ButLeds
        for (int i = 0; i < butLedList.size(); i++) {
            butLeds[i].setColor(((String) butLedList.get(i).get("color")).charAt(0));
            butLeds[i].setMapping(((Double) butLedList.get(i).get("mapping")).byteValue());
            butLeds[i].setChannel(((Double) butLedList.get(i).get("channel")).byteValue());
        }
        // - NavLeds
        for (int i = 0; i < navLedList.size(); i++) {
            navLeds[i].setStatus((Boolean) navLedList.get(i).get("status"));
            navLeds[i].setMapping(((Double) navLedList.get(i).get("mapping")).byteValue());
            navLeds[i].setChannel(((Double) navLedList.get(i).get("channel")).byteValue());
        }

        System.out.println("loadFromFile: File loaded successfully");
    }


    public void saveToFile(File file) {
        System.out.println("saveToFile: saving to " + file.getAbsolutePath());

        // Create gson object with pretty printing enabled and JSON object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject obj = new JsonObject();

        // Add all the fields to the root JSON tree
        obj.add("creation_date", gson.toJsonTree(java.time.LocalDateTime.now().toString()));
        obj.add("encoders", gson.toJsonTree(encoders));
        obj.add("potentiometers", gson.toJsonTree(potentiometers));
        obj.add("buttons", gson.toJsonTree(buttons));
        obj.add("pads", gson.toJsonTree(pads));
        obj.add("butLeds", gson.toJsonTree(butLeds));
        obj.add("navLeds", gson.toJsonTree(navLeds));
        // TODO check that there are no more structures to save

        // Write the object into the file
        Writer writer;
        try {writer = new FileWriter(file);}
        catch (Exception e) {
            System.out.println("saveToFile: Unable to write JSON object to file");
            Alert a = new Alert(Alert.AlertType.ERROR, "The file could not be written (" + e.getMessage() + ").");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.showAndWait();
            return;
        }

        try {gson.toJson(obj, writer);}
        catch (Exception e) {
            System.out.println("saveToFile: Objects could not be written to file as JSON");
            Alert a = new Alert(Alert.AlertType.ERROR, "There was a problem converting the configuration to a file (" + e.getMessage() + ").");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.showAndWait();
            return;
        }

        try {writer.close();}
        catch (Exception e) {
            System.out.println("saveToFile: File could not be closed");
            Alert a = new Alert(Alert.AlertType.ERROR, "The file could not be opened or read (" + e.getMessage() + ").");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.showAndWait();
            return;
        }

        System.out.println("saveToFile: File written successfully");
    }


    // --------------------
    // -+- for ENCODERS -+-
    // --------------------

    public char encGetRingMode(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else return encoders[id].getRingMode();
    }

    public void encSetRingMode(int id, char mode) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else encoders[id].setRingMode(mode);
    }

    public boolean encGetRelativeMode(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else return encoders[id].getRelativeMode();
    }

    public void encSetRelativeMode(int id, boolean mode) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else encoders[id].setRelativeMode(mode);
    }

    public byte encGetSpeed(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else return encoders[id].getSpeed();
    }

    public void encSetSpeed(int id, byte speed) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else encoders[id].setSpeed(speed);
    }

    public boolean encGetLocalControl(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else return encoders[id].getLocalControl();
    }

    public void encSetLocalControl(int id, boolean localControl) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else encoders[id].setLocalControl(localControl);
    }

    public byte encGetMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else return encoders[id].getMapping();
    }

    public void encSetMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else encoders[id].setMapping(mapping);
    }

    public byte encGetChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else return encoders[id].getChannel();
    }

    public void encSetChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else encoders[id].setChannel(channel);
    }


    /*public byte encGetLedMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else return encoders[id].getLedMapping();
    }

    public void encSetLedMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else encoders[id].setLedMapping(mapping);
    }

    public byte encGetLedChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else return encoders[id].getLedChannel();
    }

    public void encSetLedChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= encoders.length) throw new IllegalArgumentException();
        else encoders[id].setLedChannel(channel);
    }*/


    // --------------------------
    // -+- for POTENTIOMETERS -+-
    // --------------------------

    public byte potGetMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= potentiometers.length) throw new IllegalArgumentException();
        else return potentiometers[id].getMapping();
    }

    public void potSetMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= potentiometers.length) throw new IllegalArgumentException();
        else potentiometers[id].setMapping(mapping);
    }

    public byte potGetChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= potentiometers.length) throw new IllegalArgumentException();
        else return potentiometers[id].getChannel();
    }

    public void potSetChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= potentiometers.length) throw new IllegalArgumentException();
        else potentiometers[id].setChannel(channel);
    }


    // -------------------
    // -+- for BUTTONS -+-
    // -------------------

    public byte butGetMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else return buttons[id].getMapping();
    }

    public void butSetMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else buttons[id].setMapping(mapping);
    }

    public byte butGetChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else return buttons[id].getChannel();
    }

    public void butSetChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else buttons[id].setChannel(channel);
    }

    public boolean butGetOutputType(int id) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else return buttons[id].getOutputType();
    }

    public void butSetOutputType(int id, boolean type) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else buttons[id].setOutputType(type);
    }

    public boolean butGetSpeedControl(int id) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else return buttons[id].getSpeedControl();
    }

    public void butSetSpeedControl(int id, boolean control) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else buttons[id].setSpeedControl(control);
    }

    public boolean butGetLocalControl(int id) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else return buttons[id].getLocalControl();
    }

    public void butSetLocalControl(int id, boolean control) throws IllegalArgumentException {
        if (id < 0 || id >= buttons.length) throw new IllegalArgumentException();
        else buttons[id].setLocalControl(control);
    }


    // ----------------
    // -+- for PADS -+-
    // ----------------

    public byte padGetHitMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getHitMapping();
    }

    public void padSetHitMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setHitMapping(mapping);
    }

    public byte padGetHitChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getHitChannel();
    }

    public void padSetHitChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setHitChannel(channel);
    }

    public byte padGetRetriggerMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getRetriggerMapping();
    }

    public void padSetRetriggerMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setRetriggerMapping(mapping);
    }

    public byte padGetRetriggerChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getRetriggerChannel();
    }

    public void padSetRetriggerChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setRetriggerChannel(channel);
    }

    /*public boolean padGetCcRetrigger17(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getCcRetrigger17();
    }

    public void padSetCcRetrigger17(int id, boolean retrigger) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setCcRetrigger17(retrigger);
    }

    public boolean padGetCcRetrigger8(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getCcRetrigger8();
    }

    public void padSetCcRetrigger8(int id, boolean retrigger) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setCcRetrigger8(retrigger);
    }

    public byte padGetOnThresholdLow(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getOnThresholdLow();
    }

    public void padSetOnThresholdLow(int id, byte threshold) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setOnThresholdLow(threshold);
    }

    public byte padGetOnThresholdHigh(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getOnThresholdHigh();
    }

    public void padSetOnThresholdHigh(int id, byte threshold) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setOnThresholdHigh(threshold);
    }

    public byte padGetOffThresholdLow(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getOffThresholdLow();
    }

    public void padSetOffThresholdLow(int id, byte threshold) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setOffThresholdLow(threshold);
    }

    public byte padGetOffThresholdHigh(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getOffThresholdHigh();
    }

    public void padSetOffThresholdHigh(int id, byte threshold) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setOffThresholdHigh(threshold);
    }

    public byte padGetResendRate(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getResendRate();
    }

    public void padSetResendRate(int id, byte rate) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setResendRate(rate);
    }

    public byte padGetSensitivity(int id) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else return pads[id].getSensitivity();
    }

    public void padSetSensitivity(int id, byte sensitivity) throws IllegalArgumentException {
        if (id < 0 || id >= pads.length) throw new IllegalArgumentException();
        else pads[id].setSensitivity(sensitivity);
    }*/


    // ------------------
    // -+-  for LEDs  -+-
    // ------------------

    // Button LEDs

    public char butLedGetColor(int id) throws IllegalArgumentException {
        if (id < 0 || id >= butLeds.length) throw new IllegalArgumentException();
        else return butLeds[id].getColor();
    }

    public void butLedSetColor(int id, char color) throws IllegalArgumentException {
        if (id < 0 || id >= butLeds.length) throw new IllegalArgumentException();
        else butLeds[id].setColor(color);
    }

    public byte butLedGetMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= butLeds.length) throw new IllegalArgumentException();
        else return butLeds[id].getMapping();
    }

    public void butLedSetMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= butLeds.length) throw new IllegalArgumentException();
        else butLeds[id].setMapping(mapping);
    }

    public byte butLedGetChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= butLeds.length) throw new IllegalArgumentException();
        else return butLeds[id].getChannel();
    }

    public void butLedSetChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= butLeds.length) throw new IllegalArgumentException();
        else butLeds[id].setChannel(channel);
    }

    // Pad LEDs

    /*public boolean padLedGetStatus(int id) throws IllegalArgumentException {
        if (id < 0 || id >= padLeds.length) throw new IllegalArgumentException();
        else return padLeds[id].getStatus();
    }

    public void padLedSetStatus(int id, boolean status) throws IllegalArgumentException {
        if (id < 0 || id >= padLeds.length) throw new IllegalArgumentException();
        else padLeds[id].setStatus(status);
    }

    public byte padLedGetMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= padLeds.length) throw new IllegalArgumentException();
        else return padLeds[id].getMapping();
    }

    public void padLedSetMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= padLeds.length) throw new IllegalArgumentException();
        else padLeds[id].setMapping(mapping);
    }

    public byte padLedGetChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= padLeds.length) throw new IllegalArgumentException();
        else return padLeds[id].getChannel();
    }

    public void padLedSetChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= padLeds.length) throw new IllegalArgumentException();
        else padLeds[id].setChannel(channel);
    }*/

    // Navigation LEDs

    public boolean navLedGetStatus(int id) throws IllegalArgumentException {
        if (id < 0 || id >= navLeds.length) throw new IllegalArgumentException();
        else return navLeds[id].getStatus();
    }

    public void navLedSetStatus(int id, boolean status) throws IllegalArgumentException {
        if (id < 0 || id >= navLeds.length) throw new IllegalArgumentException();
        else navLeds[id].setStatus(status);
    }

    public byte navLedGetMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= navLeds.length) throw new IllegalArgumentException();
        else return navLeds[id].getMapping();
    }

    public void navLedSetMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= navLeds.length) throw new IllegalArgumentException();
        else navLeds[id].setMapping(mapping);
    }

    public byte navLedGetChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= navLeds.length) throw new IllegalArgumentException();
        else return navLeds[id].getChannel();
    }

    public void navLedSetChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= navLeds.length) throw new IllegalArgumentException();
        else navLeds[id].setChannel(channel);
    }

    // Encoder LEDs

    /*public boolean encLedGetStatus(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encLeds.length) throw new IllegalArgumentException();
        else return encLeds[id].getStatus();
    }

    public void encLedSetStatus(int id, boolean status) throws IllegalArgumentException {
        if (id < 0 || id >= encLeds.length) throw new IllegalArgumentException();
        else encLeds[id].setStatus(status);
    }

    public byte encLedGetMapping(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encLeds.length) throw new IllegalArgumentException();
        else return encLeds[id].getMapping();
    }

    public void encLedSetMapping(int id, byte mapping) throws IllegalArgumentException {
        if (id < 0 || id >= encLeds.length) throw new IllegalArgumentException();
        else encLeds[id].setMapping(mapping);
    }

    public byte encLedGetChannel(int id) throws IllegalArgumentException {
        if (id < 0 || id >= encLeds.length) throw new IllegalArgumentException();
        else return encLeds[id].getChannel();
    }

    public void encLedSetChannel(int id, byte channel) throws IllegalArgumentException {
        if (id < 0 || id >= encLeds.length) throw new IllegalArgumentException();
        else encLeds[id].setMapping(channel);
    }*/

    // TODO global configuration adjusters (same as previous functions but with global stuff)
}
