package app.ui;

import app.config.TweakerConfig;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;


public class TweakerModel {

    // Colors
    private static final Color UNSELECTED = Color.TRANSPARENT;
    private static final Color HOVER = Color.LIGHTGRAY;
    private static final Color SELECTED = Color.LIGHTBLUE;


    // Individual controls
    private final Circle[] encoder;
    private final Circle[] knob;
    private final Rectangle[] pad;
    private final Rectangle[] gridButton;
    private final Rectangle[] button;
    private final Rectangle[] fader;
    private final Rectangle crossfader;
    private final Circle bigEncoder;
    private final Rectangle[] navigation;
    // Model
    private final HBox model;

    // Events
    EventHandler<MouseEvent> greyOnMouseEnter;
    EventHandler<MouseEvent> transparentOnMouseExit;
    EventHandler<MouseEvent> changeVisiblePanel;


    // Auxiliary function used in the constructor to set the fill color
    private void setColorAndBorders(Shape shape) {
        shape.setFill(UNSELECTED);
        shape.setStroke(Color.GREY);
        shape.setStrokeWidth(2);
        shape.setStrokeType(StrokeType.INSIDE);
    }

    private void resetColorForAllControls() {
        for (Circle e : encoder) e.setFill(UNSELECTED);
        for (Circle k : knob) k.setFill(UNSELECTED);
        for (Rectangle p : pad) p.setFill(UNSELECTED);
        for (Rectangle g : gridButton) g.setFill(UNSELECTED);
        for (Rectangle b : button) b.setFill(UNSELECTED);
        for (Rectangle f : fader) f.setFill(UNSELECTED);
        crossfader.setFill(UNSELECTED);
        bigEncoder.setFill(UNSELECTED);
        for (Rectangle n : navigation) n.setFill(UNSELECTED);
    }


    // Constructor: Generates the whole graphical model, but does not set handlers
    public TweakerModel() {

        // Define all the controls
        // Encoders
        encoder = new Circle[6];
        for (int i = 0; i < 6; i++) {
            encoder[i] = new Circle();
            encoder[i].setId("encoder0" + i);
            encoder[i].setRadius(32);
            setColorAndBorders(encoder[i]);
        }
        // Knobs
        knob = new Circle[2];
        for (int i = 0; i < 2; i++) {
            knob[i] = new Circle();
            knob[i].setId("knob0" + i);
            knob[i].setRadius(24);
            setColorAndBorders(knob[i]);
        }
        // Pads
        pad = new Rectangle[8];
        for (int i = 0; i < 8; i++) {
            pad[i] = new Rectangle();
            pad[i].setId("pad0" + i);
            pad[i].setHeight(72);
            pad[i].setWidth(72);
            pad[i].setArcHeight(8);
            pad[i].setArcWidth(8);
            setColorAndBorders(pad[i]);
        }
        // Grid buttons
        gridButton = new Rectangle[32];
        for (int i = 0; i < 32; i++) {
            gridButton[i] = new Rectangle();
            gridButton[i].setId("gridButton" + (i < 10 ? "0" + i : i));
            gridButton[i].setHeight(34);
            gridButton[i].setWidth(34);
            gridButton[i].setArcHeight(6);
            gridButton[i].setArcWidth(6);
            setColorAndBorders(gridButton[i]);
        }
        // Buttons
        button = new Rectangle[6];
        for (int i = 0; i < 6; i++) {
            button[i] = new Rectangle();
            button[i].setId("button0" + i);
            button[i].setHeight(24);
            button[i].setWidth(46);
            button[i].setArcHeight(6);
            button[i].setArcWidth(6);
            setColorAndBorders(button[i]);
        }
        // Faders
        fader = new Rectangle[2];
        for (int i = 0; i < 2; i++) {
            fader[i] = new Rectangle();
            fader[i].setId("fader0" + i);
            fader[i].setHeight(144);
            fader[i].setWidth(20);
            fader[i].setArcHeight(2);
            fader[i].setArcWidth(2);
            setColorAndBorders(fader[i]);
        }
        // Crossfader
        crossfader = new Rectangle();
        crossfader.setId("crossfader00");
        crossfader.setHeight(20);
        crossfader.setWidth(144);
        crossfader.setArcHeight(2);
        crossfader.setArcWidth(2);
        setColorAndBorders(crossfader);
        // Big encoder
        bigEncoder = new Circle();
        bigEncoder.setId("bigEncoder00");
        bigEncoder.setRadius(40);
        setColorAndBorders(bigEncoder);
        // Navigation buttons
        navigation = new Rectangle[5];
        for (int i = 0; i < 5; i++) {
            navigation[i] = new Rectangle();
            navigation[i].setId("navigation0" + i);
            navigation[i].setHeight(24);
            navigation[i].setWidth(24);
            navigation[i].setArcHeight(8);
            navigation[i].setArcWidth(8);
            setColorAndBorders(navigation[i]);
        }

        // Now start grouping elements. We will have three columns:
        //  - Left column, with the first three encoders, two buttons, first slider and third button
        //  - Center column, with knobs, big encoder, navigation buttons, pads, grid and crossfader
        //  - Right column, symmetrical to the left one

        // First, add items to the left column
        VBox leftColumn = new VBox(
                encoder[0], encoder[1], encoder[2],
                button[0], button[1],
                fader[0],
                button[2]
        );
        leftColumn.setAlignment(Pos.CENTER);
        leftColumn.setSpacing(12);

        // The right column is symmetrical so we can create it now
        VBox rightColumn = new VBox(
                encoder[3], encoder[4], encoder[5],
                button[3], button[4],
                fader[1],
                button[5]
        );
        rightColumn.setAlignment(Pos.CENTER);
        rightColumn.setSpacing(12);

        // The center column is more complicated, since it has several parts:
        //  - A top row with knob, big encoder, navigation and knob
        //      . The navigation cluster must be a GridPane
        //  - A center row with another GridPane with the pads
        //  - A lower row with the GridPane with grid buttons
        //  - A bottom row with the crossfader

        // For the top row, first make the GridPanes with their respective elements, then put everything into the HBox
        GridPane navGrid = new GridPane();
        navGrid.setAlignment(Pos.CENTER);
        navGrid.setHgap(2);
        navGrid.setVgap(2);
        navGrid.add(navigation[0], 1, 0);
        navGrid.add(navigation[1], 1, 1);
        navGrid.add(navigation[2], 1, 2);
        navGrid.add(navigation[3], 0, 1);
        navGrid.add(navigation[4], 2, 1);

        HBox topPart = new HBox(knob[0], bigEncoder, navGrid, knob[1]);
        topPart.setAlignment(Pos.CENTER);
        topPart.setSpacing(16);

        // Continue creating the rest of the elements of the column
        GridPane padGrid = new GridPane();
        padGrid.setAlignment(Pos.CENTER);
        padGrid.setHgap(4);
        padGrid.setVgap(4);
        padGrid.add(pad[0], 0, 0);
        padGrid.add(pad[1], 1, 0);
        padGrid.add(pad[2], 2, 0);
        padGrid.add(pad[3], 3, 0);
        padGrid.add(pad[4], 0, 1);
        padGrid.add(pad[5], 1, 1);
        padGrid.add(pad[6], 2, 1);
        padGrid.add(pad[7], 3, 1);

        GridPane btnGrid = new GridPane();
        btnGrid.setAlignment(Pos.CENTER);
        btnGrid.setHgap(4);
        btnGrid.setVgap(4);
        int pos = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 8; x++) {
                btnGrid.add(gridButton[pos++], x, y);
            }
        }

        // Now add them all to the center column
        VBox centerColumn = new VBox(topPart, padGrid, btnGrid, crossfader);
        centerColumn.setAlignment(Pos.CENTER);
        centerColumn.setSpacing(24);


        // Finally, add all the columns to a HBox
        model = new HBox(leftColumn, centerColumn, rightColumn);
        model.setAlignment(Pos.CENTER);
        model.setSpacing(16);
    }


    private void createHandlers(TweakerConfig config, EditorPane editorPane) {

        // On mouse enter, change the fill color
        greyOnMouseEnter = e -> {
            // Get the calling object
            Shape shape = (Shape) e.getSource();
            // Only change color if it was unselected
            if (shape.getFill() == UNSELECTED) shape.setFill(HOVER);
        };

        // On mouse exit, put the fill color back to transparent
        transparentOnMouseExit = e -> {
            // Get the calling object
            Shape shape = (Shape) e.getSource();
            // Only change color if it was unselected
            if (shape.getFill() == HOVER) shape.setFill(UNSELECTED);
        };

        // On mouse click, hide all left panels and make one visible
        changeVisiblePanel = e -> {

            // Get the calling object and change its color
            Shape shape = (Shape) e.getSource();
            System.out.println("changeVisiblePanel: Clicked on " + shape.getId());
            // Set all controls to transparent
            resetColorForAllControls();
            // And then set the
            shape.setFill(SELECTED);

            // Hide all editor pages
            for (Node n : editorPane.getPane().getChildren()) n.setVisible(false);

            // Depending on the ID of the caller, we will open a panel or another
            //  0 -> start (hidden after clicking any control)
            //  1 -> encoder
            //  2 -> pad
            //  3 -> potentiometers
            //  4 -> button, gridButton
            //  5 -> bigEncoder

            // Get the number of the control, without the name
            int controlId = Integer.parseInt(shape.getId().substring(shape.getId().length() - 2));
            // Use the name of the control to switch between panes
            ObservableList<Node> panes = editorPane.getPane().getChildren();
            switch (shape.getId().substring(0, shape.getId().length() - 2)) {
                case "encoder":
                    editorPane.configEncoderBox(controlId);
                    panes.get(1).setVisible(true);
                    break;
                case "pad":
                    editorPane.configPadBox(controlId);
                    panes.get(2).setVisible(true);
                    break;
                case "fader":
                    editorPane.configPotentiometerBox(controlId + 2);
                    panes.get(3).setVisible(true);
                    break;
                case "crossfader":
                    editorPane.configPotentiometerBox(4);
                    panes.get(3).setVisible(true);
                    break;
                case "knob":
                    editorPane.configPotentiometerBox(controlId);
                    panes.get(3).setVisible(true);
                    break;
                case "button":
                    editorPane.configButtonBox(controlId + 32);
                    panes.get(4).setVisible(true);
                    break;
                case "navigation":
                    editorPane.configButtonBox(controlId + 38);
                    panes.get(4).setVisible(true);
                    break;
                case "gridButton":
                    editorPane.configButtonBox(controlId);
                    panes.get(4).setVisible(true);
                    break;
                case "bigEncoder":
                    editorPane.configBigEncoderBox();
                    panes.get(5).setVisible(true);
                    break;
            }
        };
    }


    public void setHandlers(TweakerConfig config, EditorPane editorPane) {

        createHandlers(config, editorPane);

        //Registering the event filter
        for (Shape i : encoder) {
            i.setOnMouseEntered(greyOnMouseEnter);
            i.setOnMouseExited(transparentOnMouseExit);
            i.setOnMouseClicked(changeVisiblePanel);
        }
        for (Shape i : knob) {
            i.setOnMouseEntered(greyOnMouseEnter);
            i.setOnMouseExited(transparentOnMouseExit);
            i.setOnMouseClicked(changeVisiblePanel);
        }
        for (Shape i : pad) {
            i.setOnMouseEntered(greyOnMouseEnter);
            i.setOnMouseExited(transparentOnMouseExit);
            i.setOnMouseClicked(changeVisiblePanel);
        }
        for (Shape i : gridButton) {
            i.setOnMouseEntered(greyOnMouseEnter);
            i.setOnMouseExited(transparentOnMouseExit);
            i.setOnMouseClicked(changeVisiblePanel);
        }
        for (Shape i : button) {
            i.setOnMouseEntered(greyOnMouseEnter);
            i.setOnMouseExited(transparentOnMouseExit);
            i.setOnMouseClicked(changeVisiblePanel);
        }
        for (Shape i : fader) {
            i.setOnMouseEntered(greyOnMouseEnter);
            i.setOnMouseExited(transparentOnMouseExit);
            i.setOnMouseClicked(changeVisiblePanel);
        }

        crossfader.setOnMouseEntered(greyOnMouseEnter);
        crossfader.setOnMouseExited(transparentOnMouseExit);
        crossfader.setOnMouseClicked(changeVisiblePanel);

        bigEncoder.setOnMouseEntered(greyOnMouseEnter);
        bigEncoder.setOnMouseExited(transparentOnMouseExit);
        bigEncoder.setOnMouseClicked(changeVisiblePanel);

        for (Shape i : navigation) {
            i.setOnMouseEntered(greyOnMouseEnter);
            i.setOnMouseExited(transparentOnMouseExit);
            i.setOnMouseClicked(changeVisiblePanel);
        }
    }


    public HBox getModel() {
        return model;
    }
}
