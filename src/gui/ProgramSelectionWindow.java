package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.statement.Statement;
import view.ExamplePrograms;

import java.util.List;

public class ProgramSelectionWindow extends Application {

    private static List<ExamplePrograms.ProgramExample> programs; // store the programs statically

    public static void setPrograms(List<ExamplePrograms.ProgramExample> programList) {
        programs = programList;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Select Program to Run");

        if (programs == null || programs.isEmpty()) {
            throw new RuntimeException("Programs list not set or empty!");
        }

        // --- ListView with descriptions ---
        ObservableList<String> programDescriptions = FXCollections.observableArrayList();
        for (ExamplePrograms.ProgramExample ex : programs) {
            programDescriptions.add(ex.getDescription());
        }

        ListView<String> programListView = new ListView<>(programDescriptions);
        programListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // --- Button ---
        Button runButton = new Button("Run Selected Program");
        runButton.setDisable(true);

        programListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            runButton.setDisable(newSelection == null);
        });

        runButton.setOnAction(e -> {
            int selectedIndex = programListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ExamplePrograms.ProgramExample selectedExample = programs.get(selectedIndex);

                Statement selectedProgram = selectedExample.getProgram();
                //TypeCheck before creating the MainWindow
                try{
                    selectedProgram.typeCheck(new model.ADT.Map.SymbolTable<String,model.type.Type>());
                    //if typeCheck passes, give a confirmation alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Type Check Successful");
                    alert.setHeaderText("Type Check Passed");
                    alert.setContentText("The selected program passed type checking.");
                    alert.showAndWait();
                }catch(exceptions.MyException ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Type Check Error");
                    alert.setHeaderText("Type Check Failed");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    return; //stop: do not open MainWindow
                }
                // Launch MainWindow
                MainWindow mainWindow = new MainWindow(selectedProgram);
                try {
                    mainWindow.start(new Stage());
                    primaryStage.close(); // optional: close selection window
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox root = new VBox(10,
                new Label("Available Programs:"),
                programListView,
                runButton
        );
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: pink;");

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}