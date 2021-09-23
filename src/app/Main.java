package app;

import app.config.TweakerConfig;
import app.ui.EditorPane;
import app.ui.TweakerModel;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {


    // This is the data model, where the whole configuration of the tweaker is saved
    TweakerConfig tweakerConfig;

    public void saveToFile() {

        File file;
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tweaker patch (.twp)", "*.twp"),
                new FileChooser.ExtensionFilter("JSON file (.json)", "*.json"));

        file = fc.showSaveDialog(null);

        try {
            tweakerConfig.saveToFile(file);
        } catch (Exception e) {
            System.out.println("Exception in saveToFile: " + e.getMessage());
        }
    }

    public void loadFromFile() {

        File file;
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tweaker patch (.twp)", "*.twp"),
                new FileChooser.ExtensionFilter("JSON file (.json)", "*.json"));

        file = fc.showOpenDialog(null);

        try {
            tweakerConfig.loadFromFile(file);
        } catch (Exception e) {
            System.out.println("Exception in loadFromFile: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {

        // Right part of the screen (right pane)

        // First, the Tweaker model
        TweakerModel tweakerModel = new TweakerModel();

        // Then, the saving and dumping buttons
        Button saveToFileButton = new Button("Save configuration to file");
        Button loadFromFileButton = new Button("Load configuration from file");
        Button dumpConfigButton = new Button("Dump configuration");
        // Set the button styles
        saveToFileButton.setMinSize(240, 32);
        loadFromFileButton.setMinSize(240, 32);
        dumpConfigButton.setMinSize(240, 32);
        // Dump helper text
        Text changesText = new Text("Never dumped.");
        changesText.setFont(Font.font(null, FontWeight.NORMAL, 14));


        // Store all these buttons and text in a grid pane
        GridPane saveButtons = new GridPane();
        saveButtons.add(saveToFileButton, 0, 0);
        saveButtons.add(loadFromFileButton, 1, 0);
        saveButtons.add(dumpConfigButton, 0, 1);
        saveButtons.add(changesText, 1, 1);
        // Set the style
        saveButtons.setAlignment(Pos.CENTER);
        saveButtons.setHgap(12);
        saveButtons.setVgap(8);

        // The model and the buttons go into a VBox
        VBox rightPane = new VBox(tweakerModel.getModel(), saveButtons);
        // Some style
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setSpacing(48);


        // Create the configuration and set the handlers
        tweakerConfig = new TweakerConfig();
        tweakerConfig.changeTextSet(changesText);
        saveToFileButton.setOnMouseClicked(mouseEvent -> {saveToFile();});
        loadFromFileButton.setOnMouseClicked(mouseEvent -> {loadFromFile();});
        dumpConfigButton.setOnMouseClicked(mouseEvent -> {tweakerConfig.dump();});
        // Left part of the screen (left pane)
        EditorPane editorPane = new EditorPane(tweakerConfig);
        // Set the handlers
        tweakerModel.setHandlers(tweakerConfig, editorPane);


        // TODO Add other things, like status bars
        HBox root = new HBox(editorPane.getPane(), rightPane);
        root.setSpacing(48);
        root.setAlignment(Pos.CENTER);

        //Creating a scene object
        Scene scene = new Scene(root, 1200, 700);
        //Setting title to the Stage
        stage.setTitle("Tweaker Editor");
        //Adding scene to the stage
        stage.setScene(scene);
        // Making it not resizable
        stage.setResizable(false);
        //Displaying the contents of the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
