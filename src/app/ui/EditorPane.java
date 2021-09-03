package app.ui;

import app.config.TweakerConfig;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class EditorPane {

    private final VBox pane;
    private final TweakerConfig config;

    // Different sections of the pane, that will open and reconfigure themselves according to the control to be
    // configured
    private VBox startBox;
    private VBox encoderBox;
    private VBox padBox;
    private VBox potentiometerBox;
    private VBox buttonBox;
    private VBox bigEncoderBox;

    // Last control of its kind selected by the user
    private int butSelected;
    private int potSelected;
    private int padSelected;
    private int encSelected;



    private Rectangle createHR() {
        Rectangle hr = new Rectangle();
        hr.setHeight(2);
        hr.setWidth(580);
        hr.setFill(Color.LIGHTGRAY);
        return hr;
    }


    // Creates all the elements inside the respective panes

    private void createStartBox() {

        Rectangle hr1 = createHR();

        Text t = new Text("Select a control to start editing.");
        t.setFont(Font.font(null, FontWeight.NORMAL, 28));

        Text t2 = new Text("Click 'Dump configuration' to save your configuration to the device.");
        t2.setFont(Font.font(null, FontWeight.NORMAL, 16));

        Rectangle hr2 = createHR();

        startBox = new VBox(hr1, t, t2, hr2);
        startBox.setSpacing(24);
    }

    private void createEncoderBox() {

        // First, an horizontal line (a rectangle)
        Rectangle hr1 = createHR();


        // Then, a HBox with the control name and the reset button
        // Control name
        Text n = new Text("Encoder X");
        n.setId("name");
        n.setFont(Font.font(null, FontWeight.NORMAL, 24));
        // Edit button button
        Button e = new Button("Edit associated button");
        e.setId("bAssociated");
        // Reset button
        Button r = new Button("Reset");
        r.setId("bReset");
        // Put them in an HBox
        HBox secName = new HBox(n, e, r);
        secName.setMaxWidth(580);
        secName.setAlignment(Pos.CENTER_LEFT);
        secName.setSpacing(20);


        // Now a grid with three columns: one for parameter name, other for value, and other for description
        GridPane secParam = new GridPane();
        secParam.setVgap(24);
        secParam.setHgap(30);

        // Parameter name column
        Text p1 = new Text("Ring mode:");
        p1.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p2 = new Text("Output mode:");
        p2.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p3 = new Text("Speed:");
        p3.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p4 = new Text("Ring local control:");
        p4.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p5 = new Text("Mapping:");
        p5.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p6 = new Text("Channel:");
        p6.setFont(Font.font(null, FontWeight.NORMAL, 16));
        secParam.add(p1, 0, 0);
        secParam.add(p2, 0, 1);
        secParam.add(p3, 0, 2);
        secParam.add(p4, 0, 3);
        secParam.add(p5, 0, 4);
        secParam.add(p6, 0, 5);

        // Value column
        // - Ring mode
        String[] values = {"Fill", "Walk", "Eq", "Spread"};
        ChoiceBox<String> v1 = new ChoiceBox<>(FXCollections.observableArrayList(values));
        v1.setId("vRing");
        v1.setPrefWidth(100);
        // - Relative mode (Output mode)
        values = new String[]{"Absolute", "Relative"};
        ChoiceBox<String> v2 = new ChoiceBox<>(FXCollections.observableArrayList(values));
        v2.setId("vOutput");
        v2.setPrefWidth(100);
        // - Speed
        Spinner<Integer> v3 = new Spinner<>(1, 7, 3);
        v3.setId("vSpeed");
        v3.setPrefWidth(100);
        // - Ring local control
        CheckBox v4 = new CheckBox("Enabled");
        v4.setId("vLocal");
        v4.setPrefWidth(100);
        // - Mapping number
        Spinner<Integer> v5 = new Spinner<>(0, 127, 0);
        v5.setId("vMapping");
        v5.setPrefWidth(100);
        // - Channel number
        Spinner<Integer> v6 = new Spinner<>(1, 16, 1);
        v6.setId("vChannel");
        v6.setPrefWidth(100);

        secParam.add(v1, 1, 0);
        secParam.add(v2, 1, 1);
        secParam.add(v3, 1, 2);
        secParam.add(v4, 1, 3);
        secParam.add(v5, 1, 4);
        secParam.add(v6, 1, 5);

        // Description column
        Text d1 = new Text("Change how the LED ring lights up.");
        Text d2 = new Text("Sets the MIDI output to absolute or relative.");
        Text d3 = new Text("Sets the MIDI value difference between steps.");
        Text d4 = new Text("Make the LED ring follow the encoder value.");
        Text d5 = new Text("Set the MIDI CC number the encoder sends.");
        Text d6 = new Text("Set the MIDI channel where the CC is sent.");
        secParam.add(d1, 2, 0);
        secParam.add(d2, 2, 1);
        secParam.add(d3, 2, 2);
        secParam.add(d4, 2, 3);
        secParam.add(d5, 2, 4);
        secParam.add(d6, 2, 5);


        // Then, another horizontal line
        Rectangle hr2 = createHR();


        // Propagation section
        CheckBox prk = new CheckBox("Keep MIDI mappings");
        Text prt1 = new Text("Propagate changes ( ");
        Text prt2 = new Text(" ) to:");
        // The All button is always the same but the others may change
        Button pr1 = new Button("All encoders");
        pr1.setId("propagateAll");
        Button pr2 = new Button("Row");
        pr2.setId("propagateRow");
        Button pr3 = new Button("Column");
        pr3.setId("propagateColumn");
        HBox prh = new HBox(pr1, pr2, pr3);
        prh.setSpacing(24);
        VBox secPropg = new VBox(new HBox(prt1, prk, prt2), prh);
        secPropg.setSpacing(24);


        // Last horizontal line
        Rectangle hr3 = createHR();


        encoderBox = new VBox(hr1, secName, secParam, hr2, secPropg, hr3);
        encoderBox.setSpacing(24);


        // ------------------------
        // -+- Control handlers -+-
        // ------------------------

        // - LED ring mode
        v1.setOnAction(event -> {
            char mode = v1.getValue().substring(0, 1).toLowerCase().toCharArray()[0];
            config.encSetRingMode(encSelected, mode);
        });
        // - Output mode
        v2.setOnAction(event -> {
            config.encSetRelativeMode(encSelected, v2.getValue().equals("Relative"));
        });
        // - Speed
        v3.valueProperty().addListener((obs, oldV, newV) -> {
            config.encSetSpeed(encSelected, newV.byteValue());
        });
        // - Ring local control
        v4.setOnAction(event -> {
            config.encSetLocalControl(encSelected, v4.isSelected());
        });
        // - Mapping number
        v5.valueProperty().addListener((obs, oldV, newV) -> {
            config.encSetMapping(encSelected, newV.byteValue());
        });
        // - Channel number
        v6.valueProperty().addListener((obs, oldV, newV) -> {
            config.encSetChannel(encSelected, newV.byteValue());
        });
    }

    private void createPadBox() {

        // First, an horizontal line (a rectangle)
        Rectangle hr1 = createHR();


        // Then, a HBox with the control name and the reset button
        // Control name
        Text n = new Text("Pad X");
        n.setId("name");
        n.setFont(Font.font(null, FontWeight.NORMAL, 24));
        // Global pad config button
        Button e = new Button("Edit global pad configuration");
        e.setId("bGlobal");
        // Reset button
        Button r = new Button("Reset");
        r.setId("bReset");
        // Put them in an HBox
        HBox secName = new HBox(n, e, r);
        secName.setMaxWidth(580);
        secName.setAlignment(Pos.CENTER_LEFT);
        secName.setSpacing(20);


        // Now a grid with three columns: one for parameter name, other for value, and other for description
        GridPane secParam = new GridPane();
        secParam.setVgap(24);
        secParam.setHgap(30);

        // Parameter name column
        Text p1 = new Text("Hit note mapping:");
        p1.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p2 = new Text("Hit note channel:");
        p2.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p3 = new Text("Retrigger CC mapping:");
        p3.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p4 = new Text("Retrigger CC channel:");
        p4.setFont(Font.font(null, FontWeight.NORMAL, 16));
        secParam.add(p1, 0, 0);
        secParam.add(p2, 0, 1);
        secParam.add(p3, 0, 2);
        secParam.add(p4, 0, 3);

        // Value column
        // - Hit mapping number
        Spinner<Integer> v1 = new Spinner<>(0, 127, 0);
        v1.setId("vHitMapping");
        v1.setPrefWidth(100);
        // - Hit channel number
        Spinner<Integer> v2 = new Spinner<>(1, 16, 1);
        v2.setId("vHitChannel");
        v2.setPrefWidth(100);
        // - Retrigger mapping number
        Spinner<Integer> v3 = new Spinner<>(0, 127, 0);
        v3.setId("vRetMapping");
        v3.setPrefWidth(100);
        // - Retrigger channel number
        Spinner<Integer> v4 = new Spinner<>(1, 16, 1);
        v4.setId("vRetChannel");
        v4.setPrefWidth(100);

        secParam.add(v1, 1, 0);
        secParam.add(v2, 1, 1);
        secParam.add(v3, 1, 2);
        secParam.add(v4, 1, 3);

        // Description column
        Text d1 = new Text("Set the MIDI note sent with the first hit.");
        Text d2 = new Text("Set the MIDI note channel for the hit.");
        Text d3 = new Text("Set the MIDI CC sent with the retrigger.");
        Text d4 = new Text("Set the MIDI CC channel for the retrigger.");
        secParam.add(d1, 2, 0);
        secParam.add(d2, 2, 1);
        secParam.add(d3, 2, 2);
        secParam.add(d4, 2, 3);


        // Then, another horizontal line
        Rectangle hr2 = createHR();


        // Propagation section
        CheckBox prk = new CheckBox("Keep MIDI mappings");
        Text prt1 = new Text("Propagate changes ( ");
        Text prt2 = new Text(" ) to:");
        // The All button is always the same but the others may change
        Button pr1 = new Button("All pads");
        pr1.setId("propagateAll");
        Button pr2 = new Button("Row");
        pr2.setId("propagateRow");
        Button pr3 = new Button("Column");
        pr3.setId("propagateColumn");
        HBox prh = new HBox(pr1, pr2, pr3);
        prh.setSpacing(24);
        VBox secPropg = new VBox(new HBox(prt1, prk, prt2), prh);
        secPropg.setSpacing(24);


        // Last horizontal line
        Rectangle hr3 = createHR();


        padBox = new VBox(hr1, secName, secParam, hr2, secPropg, hr3);
        padBox.setSpacing(24);

        // ------------------------
        // -+- Control handlers -+-
        // ------------------------

        // - Hit mapping
        v1.valueProperty().addListener((obs, oldV, newV) -> {
            config.padSetHitMapping(padSelected, newV.byteValue());
        });
        // - Hit channel
        v2.valueProperty().addListener((obs, oldV, newV) -> {
            config.padSetHitChannel(padSelected, newV.byteValue());
        });
        // - Retrigger mapping
        v3.valueProperty().addListener((obs, oldV, newV) -> {
            config.padSetRetriggerMapping(padSelected, newV.byteValue());
        });
        // - Retrigger channel
        v4.valueProperty().addListener((obs, oldV, newV) -> {
            config.padSetRetriggerChannel(padSelected, newV.byteValue());
        });
    }

    private void createPotentiometerBox() {

        // First, an horizontal line (a rectangle)
        Rectangle hr1 = createHR();


        // Then, a HBox with the control name and the reset button
        // Control name
        Text n = new Text("Fader X");
        n.setId("name");
        n.setFont(Font.font(null, FontWeight.NORMAL, 24));
        // Reset button
        Button r = new Button("Reset");
        r.setId("bReset");
        // Put them in an HBox
        HBox secName = new HBox(n, r);
        secName.setMaxWidth(580);
        secName.setAlignment(Pos.CENTER_LEFT);
        secName.setSpacing(20);


        // Now a grid with three columns: one for parameter name, other for value, and other for description
        GridPane secParam = new GridPane();
        secParam.setVgap(24);
        secParam.setHgap(30);

        // Parameter name column
        Text p1 = new Text("Mapping:");
        p1.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p2 = new Text("Channel:");
        p2.setFont(Font.font(null, FontWeight.NORMAL, 16));
        secParam.add(p1, 0, 0);
        secParam.add(p2, 0, 1);

        // Value column
        // - Mapping number
        Spinner<Integer> v1 = new Spinner<>(0, 127, 0);
        v1.setId("vMapping");
        v1.setPrefWidth(100);
        // - Channel number
        Spinner<Integer> v2 = new Spinner<>(1, 16, 1);
        v2.setId("vChannel");
        v2.setPrefWidth(100);

        secParam.add(v1, 1, 0);
        secParam.add(v2, 1, 1);

        // Description column
        Text d1 = new Text("Set the MIDI number sent as Note and CC.");
        Text d2 = new Text("Set the MIDI channel for the position and detent.");
        secParam.add(d1, 2, 0);
        secParam.add(d2, 2, 1);


        // Then, another horizontal line
        Rectangle hr2 = createHR();


        // Propagation section
        CheckBox prk = new CheckBox("Keep MIDI mappings");
        Text prt1 = new Text("Propagate changes ( ");
        Text prt2 = new Text(" ) to:");
        // The All button is always the same but the others may change
        Button pr1 = new Button("Faders and crossfader");
        pr1.setId("propagateAll");
        Button pr2 = new Button("Faders only");
        pr2.setId("propagateFadersOnly");
        HBox prh = new HBox(pr1, pr2);
        prh.setSpacing(24);
        VBox secPropg = new VBox(new HBox(prt1, prk, prt2), prh);
        secPropg.setSpacing(24);


        // Last horizontal line
        Rectangle hr3 = createHR();


        potentiometerBox = new VBox(hr1, secName, secParam, hr2, secPropg, hr3);
        potentiometerBox.setSpacing(24);


        // ------------------------
        // -+- Control handlers -+-
        // ------------------------

        // - Mapping
        v1.valueProperty().addListener((obs, oldV, newV) -> {
            config.potSetMapping(potSelected, newV.byteValue());
        });
        // - Channel
        v2.valueProperty().addListener((obs, oldV, newV) -> {
            config.potSetChannel(potSelected, newV.byteValue());
        });
    }

    private void createButtonBox() {

        // First, an horizontal line (a rectangle)
        Rectangle hr1 = createHR();


        // Then, a HBox with the control name and the reset button
        // Control name
        Text n = new Text("Button X");
        n.setId("name");
        n.setFont(Font.font(null, FontWeight.NORMAL, 24));
        // Reset button
        Button r = new Button("Reset");
        r.setId("bReset");
        // Put them in an HBox
        HBox secName = new HBox(n, r);
        secName.setMaxWidth(580);
        secName.setAlignment(Pos.CENTER_LEFT);
        secName.setSpacing(20);


        // Now a grid with three columns: one for parameter name, other for value, and other for description
        GridPane secParam = new GridPane();
        secParam.setVgap(24);
        secParam.setHgap(30);

        // Parameter name column
        Text p1 = new Text("Encoder speed control:");
        p1.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p2 = new Text("Local control:");
        p2.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p3 = new Text("Mapping:");
        p3.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p4 = new Text("Channel:");
        p4.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p5 = new Text("Output type:");
        p5.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p6 = new Text("LED mapping:");
        p6.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p7 = new Text("LED channel:");
        p7.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p8 = new Text("LED status:");
        p8.setFont(Font.font(null, FontWeight.NORMAL, 16));

        secParam.add(p1, 0, 0);
        secParam.add(p2, 0, 1);
        secParam.add(p3, 0, 2);
        secParam.add(p4, 0, 3);
        secParam.add(p5, 0, 4);
        secParam.add(p6, 0, 5);
        secParam.add(p7, 0, 6);
        secParam.add(p8, 0, 7);

        // Value column
        // - Encoder speed control
        CheckBox v1 = new CheckBox("Enabled");
        v1.setId("vSpeedControl");
        v1.setPrefWidth(100);
        // - Local control
        CheckBox v2 = new CheckBox("Enabled");
        v2.setId("vLocalControl");
        v2.setPrefWidth(100);
        // - Mapping number
        Spinner<Integer> v3 = new Spinner<>(0, 127, 0);
        v3.setId("vMapping");
        v3.setPrefWidth(100);
        // - Channel number
        Spinner<Integer> v4 = new Spinner<>(1, 16, 1);
        v4.setId("vChannel");
        v4.setPrefWidth(100);
        // - Output type
        String[] values = {"CC", "Note"};
        ChoiceBox<String> v5 = new ChoiceBox<>(FXCollections.observableArrayList(values));
        v5.setId("vOutputType");
        v5.setPrefWidth(100);
        // - LED Mapping number
        Spinner<Integer> v6 = new Spinner<>(0, 127, 0);
        v6.setId("vLedMapping");
        v6.setPrefWidth(100);
        // - LED Channel number
        Spinner<Integer> v7 = new Spinner<>(1, 16, 1);
        v7.setId("vLedChannel");
        v7.setPrefWidth(100);
        // - LED status
        values = new String[]{"Off", "Green", "Red", "Yellow", "Blue", "Cyan", "Magenta", "White"};
        ChoiceBox<String> v8 = new ChoiceBox<>(FXCollections.observableArrayList(values));
        v8.setId("vLedStatus");
        v8.setPrefWidth(100);

        secParam.add(v1, 1, 0);
        secParam.add(v2, 1, 1);
        secParam.add(v3, 1, 2);
        secParam.add(v4, 1, 3);
        secParam.add(v5, 1, 4);
        secParam.add(v6, 1, 5);
        secParam.add(v7, 1, 6);
        secParam.add(v8, 1, 7);

        // Description column
        Text d1 = new Text("Set this button as an encoder speed switch.");
        Text d2 = new Text("Set LED local control (light up when pressed).");
        Text d3 = new Text("Set the MIDI CC number the encoder sends.");
        Text d4 = new Text("Set the MIDI channel where the CC is sent.");
        Text d5 = new Text("Make the button send a CC or a Note message.");
        Text d6 = new Text("MIDI Note to be received for the LED to light up.");
        Text d7 = new Text("MIDI channel the LED listens to.");
        Text d8 = new Text("Set whether the LED is on and its color (if RGB).");
        secParam.add(d1, 2, 0);
        secParam.add(d2, 2, 1);
        secParam.add(d3, 2, 2);
        secParam.add(d4, 2, 3);
        secParam.add(d5, 2, 4);
        secParam.add(d6, 2, 5);
        secParam.add(d7, 2, 6);
        secParam.add(d8, 2, 7);


        // Then, another horizontal line
        Rectangle hr2 = createHR();


        // Propagation section
        CheckBox prk = new CheckBox("Keep MIDI mappings");
        Text prt1 = new Text("Propagate changes ( ");
        Text prt2 = new Text(" ) to:");
        // The All button is always the same but the others may change
        Button pr1 = new Button("All buttons");
        pr1.setId("propagateAll");
        Button pr2 = new Button("All grid buttons");
        pr2.setId("propagateGridButtons");
        Button pr3 = new Button("All control buttons");
        pr3.setId("propagateControlButtons");
        Button pr4 = new Button("Column");
        pr4.setId("propagateColumn");
        Button pr5 = new Button("Row");
        pr5.setId("propagateRow");
        HBox prh = new HBox(pr1, pr2, pr3, pr4, pr5);
        prh.setSpacing(24);
        VBox secPropg = new VBox(new HBox(prt1, prk, prt2), prh);
        secPropg.setSpacing(24);


        // Last horizontal line
        Rectangle hr3 = createHR();


        buttonBox = new VBox(hr1, secName, secParam, hr2, secPropg, hr3);
        buttonBox.setSpacing(24);


        // ------------------------
        // -+- Control handlers -+-
        // ------------------------

        // - Encoder speed control
        v1.setOnAction(event -> {
            config.butSetSpeedControl(butSelected, v1.isSelected());
        });
        // - Local control
        v2.setOnAction(event -> {
            config.butSetLocalControl(butSelected, v1.isSelected());
        });
        // - Mapping number
        v3.valueProperty().addListener((obs, oldV, newV) -> {
            config.butSetMapping(butSelected, newV.byteValue());
        });
        // - Channel number
        v4.valueProperty().addListener((obs, oldV, newV) -> {
            config.butSetChannel(butSelected, newV.byteValue());
        });
        // - Output type
        v5.setOnAction(event -> {
            config.butSetOutputType(butSelected, v5.getValue().equals("Note"));
        });
        // - LED Mapping number
        v6.valueProperty().addListener((obs, oldV, newV) -> {
            if (butSelected >= 38) config.navLedSetMapping(butSelected - 38, newV.byteValue());
            else config.butLedSetMapping(butSelected, newV.byteValue());
        });
        // - LED Channel number
        v7.valueProperty().addListener((obs, oldV, newV) -> {
            if (butSelected >= 38) config.navLedSetChannel(butSelected - 38, newV.byteValue());
            else config.butLedSetChannel(butSelected, newV.byteValue());
        });
        // - LED status
        v8.setOnAction(event -> {
            // Need this 'if' because this action is triggered in some cases we don't want
            if (v8.getValue() != null) {
                char color = v8.getValue().substring(0, 1).toLowerCase().toCharArray()[0];
                if (butSelected >= 38) config.navLedSetStatus(butSelected - 38, color != 'o');
                else config.butLedSetColor(butSelected, color);
            }
        });

    }

    private void createBigEncoderBox() {

        // First, an horizontal line (a rectangle)
        Rectangle hr1 = createHR();


        // Then, a HBox with the control name and the reset button
        // Control name
        Text n = new Text("Big encoder");
        n.setId("name");
        n.setFont(Font.font(null, FontWeight.NORMAL, 24));
        // Reset button
        Button r = new Button("Reset");
        r.setId("bReset");
        // Put them in an HBox
        HBox secName = new HBox(n, r);
        secName.setMaxWidth(580);
        secName.setAlignment(Pos.CENTER_LEFT);
        secName.setSpacing(20);


        // Now a grid with three columns: one for parameter name, other for value, and other for description
        GridPane secParam = new GridPane();
        secParam.setVgap(24);
        secParam.setHgap(30);

        // Parameter name column
        Text p1 = new Text("Output mode:");
        p1.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p2 = new Text("Speed:");
        p2.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p3 = new Text("Mapping:");
        p3.setFont(Font.font(null, FontWeight.NORMAL, 16));
        Text p4 = new Text("Channel:");
        p4.setFont(Font.font(null, FontWeight.NORMAL, 16));
        secParam.add(p1, 0, 0);
        secParam.add(p2, 0, 1);
        secParam.add(p3, 0, 2);
        secParam.add(p4, 0, 3);

        // Value column
        // - Relative mode (Output mode)
        String[] values = {"Absolute", "Relative"};
        ChoiceBox<String> v1 = new ChoiceBox<>(FXCollections.observableArrayList(values));
        v1.setId("vOutput");
        v1.setPrefWidth(100);
        // - Speed
        Spinner<Integer> v2 = new Spinner<>(1, 7, 3);
        v2.setId("vSpeed");
        v2.setPrefWidth(100);
        // - Mapping number
        Spinner<Integer> v3 = new Spinner<>(0, 127, 0);
        v3.setId("vMapping");
        v3.setPrefWidth(100);
        // - Channel number
        Spinner<Integer> v4 = new Spinner<>(1, 16, 1);
        v4.setId("vChannel");
        v4.setPrefWidth(100);

        secParam.add(v1, 1, 0);
        secParam.add(v2, 1, 1);
        secParam.add(v3, 1, 2);
        secParam.add(v4, 1, 3);

        // Description column
        Text d1 = new Text("Sets the MIDI output to absolute or relative.");
        Text d2 = new Text("Sets the MIDI value difference between steps.");
        Text d3 = new Text("Set the MIDI CC number the encoder sends.");
        Text d4 = new Text("Set the MIDI channel where the CC is sent.");
        secParam.add(d1, 2, 0);
        secParam.add(d2, 2, 1);
        secParam.add(d3, 2, 2);
        secParam.add(d4, 2, 3);


        // Then, another horizontal line
        Rectangle hr2 = createHR();


        bigEncoderBox = new VBox(hr1, secName, secParam, hr2);
        bigEncoderBox.setSpacing(24);


        // ------------------------
        // -+- Control handlers -+-
        // ------------------------

        // - Output mode
        v1.setOnAction(event -> {
            config.encSetRelativeMode(0, v1.getValue().equals("Relative"));
        });
        // - Speed
        v2.valueProperty().addListener((obs, oldV, newV) -> {
            config.encSetSpeed(0, newV.byteValue());
        });
        // - Mapping number
        v3.valueProperty().addListener((obs, oldV, newV) -> {
            config.encSetMapping(0, newV.byteValue());
        });
        // - Channel number
        v4.valueProperty().addListener((obs, oldV, newV) -> {
            config.encSetChannel(0, newV.byteValue());
        });
    }



    // These functions change the values of each pane to reflect the ones present
    // in the data model (TweakerConfig instance)

    public void configEncoderBox(int control) {

        if (control < 0 || control >= 6) {
            System.out.println("configEncoderBox: The 'control' argument is invalid");
            return;
        }

        // Configure the encoderBox stuff according to the config values
        // Set the correct name
        Text t = (Text) encoderBox.lookup("#name");
        t.setText("Encoder " + (control + 1));

        // Add one because the first encoder is the big one
        control++;
        // Needed for the lambdas that edit the actual config
        encSelected = control;

        // Get the current values for each control and set them in the UI
        // - Ring mode
        ChoiceBox<String> r = (ChoiceBox<String>) encoderBox.lookup("#vRing");
        switch (config.encGetRingMode(control)) {
            case 'f' -> r.setValue("Fill");
            case 'w' -> r.setValue("Walk");
            case 'e' -> r.setValue("Eq");
            case 's' -> r.setValue("Spread");
        }
        // - Output mode
        ChoiceBox<String> o = (ChoiceBox<String>) encoderBox.lookup("#vOutput");
        if (config.encGetRelativeMode(control)) {
            o.setValue("Relative");
        } else {
            o.setValue("Absolute");
        }
        // - Speed
        Spinner<Integer> s = (Spinner<Integer>) encoderBox.lookup("#vSpeed");
        s.getValueFactory().setValue((int) config.encGetSpeed(control));
        // - Ring local control
        CheckBox l = (CheckBox) encoderBox.lookup("#vLocal");
        l.setSelected(config.encGetLocalControl(control));
        // - Mapping
        Spinner<Integer> m = (Spinner<Integer>) encoderBox.lookup("#vMapping");
        m.getValueFactory().setValue((int) config.encGetMapping(control));
        // - Channel
        Spinner<Integer> c = (Spinner<Integer>) encoderBox.lookup("#vChannel");
        c.getValueFactory().setValue((int) config.encGetChannel(control));
    }

    public void configPadBox(int control) {

        if (control < 0 || control >= 8) {
            System.out.println("configPadBox: The 'control' argument is invalid");
            return;
        }

        // Configure the padBox stuff according to the config values
        // Set the correct name
        Text t = (Text) padBox.lookup("#name");
        t.setText("Pad " + (control + 1));

        // Needed for the lambdas that edit the actual config
        padSelected = control;

        // Values
        // - Hit note mapping
        Spinner<Integer> hm = (Spinner<Integer>) padBox.lookup("#vHitMapping");
        hm.getValueFactory().setValue((int) config.padGetHitMapping(control));
        // - Hit note channel
        Spinner<Integer> hc = (Spinner<Integer>) padBox.lookup("#vHitChannel");
        hc.getValueFactory().setValue((int) config.padGetHitChannel(control));
        // - Retrigger CC mapping
        Spinner<Integer> rm = (Spinner<Integer>) padBox.lookup("#vRetMapping");
        rm.getValueFactory().setValue((int) config.padGetRetriggerMapping(control));
        // - Retrigger CC channel
        Spinner<Integer> rc = (Spinner<Integer>) padBox.lookup("#vRetChannel");
        rc.getValueFactory().setValue((int) config.padGetRetriggerChannel(control));

        // TODO set the button handlers
    }

    public void configPotentiometerBox(int control) {

        if (control < 0 || control >= 5) {
            System.out.println("configFaderBox: The 'control' argument is invalid");
            return;
        }

        // Set the correct name
        Text t = (Text) potentiometerBox.lookup("#name");
        if (control == 4) {
            t.setText("Crossfader");
        } else if (control > 1) {
            t.setText("Fader " + (control - 1));
        } else {
            t.setText("Knob " + (control + 1));
        }

        potSelected = control;

        // - Mapping
        Spinner<Integer> m = (Spinner<Integer>) potentiometerBox.lookup("#vMapping");
        m.getValueFactory().setValue((int) config.potGetMapping(control));
        // - Channel
        Spinner<Integer> c = (Spinner<Integer>) potentiometerBox.lookup("#vChannel");
        c.getValueFactory().setValue((int) config.potGetChannel(control));

        // TODO set the button handlers
    }

    public void configButtonBox(int control) {

        if (control < 0 || control >= 43) {
            System.out.println("configFaderBox: The 'control' argument is invalid");
            return;
        }

        // Set the correct name
        Text t = (Text) buttonBox.lookup("#name");
        if (control >= 38) {
            t.setText("Navigation button " + (control - 37));
        } else if (control >= 32 && control < 38) {
            t.setText("Button " + (control - 31));
        } else {
            t.setText("Grid button " + (control + 1));
        }

        butSelected = control;


        // - Encoder speed control
        CheckBox s = (CheckBox) buttonBox.lookup("#vSpeedControl");
        s.setSelected(config.butGetSpeedControl(control));
        // - Local control
        CheckBox l = (CheckBox) buttonBox.lookup("#vLocalControl");
        l.setSelected(config.butGetLocalControl(control));
        // - Mapping
        Spinner<Integer> m = (Spinner<Integer>) buttonBox.lookup("#vMapping");
        m.getValueFactory().setValue((int) config.butGetMapping(control));
        // - Channel
        Spinner<Integer> c = (Spinner<Integer>) buttonBox.lookup("#vChannel");
        c.getValueFactory().setValue((int) config.butGetChannel(control));
        // - Output type
        ChoiceBox<String> o = (ChoiceBox<String>) buttonBox.lookup("#vOutputType");
        if (config.butGetOutputType(control)) {
            o.setValue("Note");
        } else {
            o.setValue("CC");
        }
        // - LED Mapping
        Spinner<Integer> lm = (Spinner<Integer>) buttonBox.lookup("#vLedMapping");
        if (control >= 38) lm.getValueFactory().setValue((int) config.navLedGetMapping(control - 38));
        else lm.getValueFactory().setValue((int) config.butLedGetMapping(control));
        // - LED Channel
        Spinner<Integer> lc = (Spinner<Integer>) buttonBox.lookup("#vLedChannel");
        if (control >= 38) lc.getValueFactory().setValue((int) config.navLedGetChannel(control - 38));
        else lc.getValueFactory().setValue((int) config.butLedGetChannel(control));
        // - LED status
        ChoiceBox<String> ls = (ChoiceBox<String>) buttonBox.lookup("#vLedStatus");
        // Do different stuff for navigation buttons (monochrome) and RGB buttons
        if (control >= 38) {
            ls.setItems(FXCollections.observableArrayList("On", "Off"));
            if (config.navLedGetStatus(control - 38)) ls.setValue("On");
            else ls.setValue("Off");
        } else {
            ls.setItems(FXCollections.observableArrayList("Off", "Green", "Red", "Yellow", "Blue", "Cyan", "Magenta", "White"));
            switch (config.butLedGetColor(control)) {
                // (o)ff, (g)reen, (r)ed, (y)ellow, (b)lue, (c)yan, (m)agenta, (w)hite
                case 'o' -> ls.setValue("Off");
                case 'g' -> ls.setValue("Green");
                case 'r' -> ls.setValue("Red");
                case 'y' -> ls.setValue("Yellow");
                case 'b' -> ls.setValue("Blue");
                case 'c' -> ls.setValue("Cyan");
                case 'm' -> ls.setValue("Magenta");
                case 'w' -> ls.setValue("White");
                default -> ls.setValue("Off");
            }
        }

    }

    public void configBigEncoderBox() {

        // - Output mode
        ChoiceBox<String> o = (ChoiceBox<String>) bigEncoderBox.lookup("#vOutput");
        if (config.encGetRelativeMode(0)) {
            o.setValue("Relative");
        } else {
            o.setValue("Absolute");
        }
        // - Speed
        Spinner<Integer> s = (Spinner<Integer>) bigEncoderBox.lookup("#vSpeed");
        s.getValueFactory().setValue((int) config.encGetSpeed(0));
        // - Mapping
        Spinner<Integer> m = (Spinner<Integer>) bigEncoderBox.lookup("#vMapping");
        m.getValueFactory().setValue((int) config.encGetMapping(0));
        // - Channel
        Spinner<Integer> c = (Spinner<Integer>) bigEncoderBox.lookup("#vChannel");
        c.getValueFactory().setValue((int) config.encGetChannel(0));
    }


    // Create all the elements
    public EditorPane(TweakerConfig c) {

        // Copy the reference to the TweakerConfig object, which we will use to edit the local configuration of the
        // hardware before dumping it
        config = c;

        // Call all the createXbox
        createStartBox();
        createEncoderBox();
        createPadBox();
        createPotentiometerBox();
        createButtonBox();
        createBigEncoderBox();

        // (check inside createHandlers() in TweakerModel to verify this index)
        //  0 -> start
        //  1 -> encoder
        //  2 -> pad
        //  3 -> potentiometers
        //  4 -> button, gridButton
        //  5 -> bigEncoder

        // Bind the managed property to the visible so that when a pane is hidden, it
        // has zero height
        startBox.managedProperty().bind(startBox.visibleProperty());
        encoderBox.setVisible(false);
        encoderBox.managedProperty().bind(encoderBox.visibleProperty());
        padBox.setVisible(false);
        padBox.managedProperty().bind(padBox.visibleProperty());
        potentiometerBox.setVisible(false);
        potentiometerBox.managedProperty().bind(potentiometerBox.visibleProperty());
        buttonBox.setVisible(false);
        buttonBox.managedProperty().bind(buttonBox.visibleProperty());
        bigEncoderBox.setVisible(false);
        bigEncoderBox.managedProperty().bind(bigEncoderBox.visibleProperty());

        pane = new VBox(startBox, encoderBox, padBox, potentiometerBox, buttonBox, bigEncoderBox);
        pane.setAlignment(Pos.CENTER);
    }


    public VBox getPane() {
        return pane;
    }
}
