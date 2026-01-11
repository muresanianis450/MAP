package gui;

import javafx.application.Application;
import model.statement.Statement;
import view.ExamplePrograms;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {

        // Load all examples
        List<ExamplePrograms.ProgramExample> examples = ExamplePrograms.getPrograms();

        // Extract the Statements and descriptions for ProgramSelectionWindow
        ProgramSelectionWindow.setPrograms(examples);

        // Launch the selection window
        Application.launch(ProgramSelectionWindow.class, args);
    }
}
