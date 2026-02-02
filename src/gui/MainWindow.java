package gui;

import controller.GUIController;
import exceptions.MyException;
import model.ADT.BarrierTable.BarrierEntry;
import model.ADT.FileTable.IFileTable;
import model.ADT.Heap.IHeap;
import model.ADT.List.IList;
import model.ADT.LockTable.ILockTable;
import model.ADT.Map.IMap;
import model.ADT.Stack.IStack;
import model.state.ProgramState;
import model.statement.Statement;
import model.value.Value;
import repository.Repository;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import model.ADT.ProcTable.IProcTable;
import model.ADT.ProcTable.ProcTable;
import model.ADT.Map.SymbolTable;

import model.ADT.BarrierTable.BarrierTable;
import model.ADT.BarrierTable.IBarrierTable;

public class MainWindow extends Application {

    private Statement program; // Selected program
    private GUIController controller;
    private ProgramState programState;
    private Repository repo;

    // --- JavaFX UI components ---
    private TextField nrPrgStatesField;
    private TableView<HeapEntry> heapTable;
    private TableView<SymTableEntry> symTable;
    private ListView<String> outList;
    private ListView<String> fileTableList;
    private ListView<Integer> prgStateIdsList;
    private ListView<String> exeStackList;
    private Button runOneStepButton;

    private TableView<BarrierEntryRow> barrierTableView;
    public MainWindow(Statement program) {
        this.program = program;
    }


    public static class BarrierEntryRow{
        private final Integer index;
        private final Integer value;
        private final String list;

        public BarrierEntryRow(Integer index, Integer value, String list) {
            this.index = index;
            this.value = value;
            this.list = list;
        }
        public Integer getIndex() { return index;}
        public Integer getValue() { return value;}
        public String getList() { return list; }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Interpreter GUI - Main Window");

        // --- Initialize ProgramState, Repository, Controller ---
        IStack<Statement> exeStack = new model.ADT.Stack.StackExecutionStack<>();
        IMap<String, Value> symTableMap = new model.ADT.Map.SymbolTable<>();
        IList<Value> out = new model.ADT.List.ListOut<>();
        IHeap heap = new model.ADT.Heap.Heap();
        IFileTable fileTable = new model.ADT.FileTable.FileTable();
        ILockTable lockTable = new model.ADT.LockTable.LockTable();
        IBarrierTable barrierTable = new BarrierTable();

        //PROCEDURES
        IStack<IMap<String,Value>> symTableStack = new model.ADT.Stack.StackExecutionStack<>();
        symTableStack.push(new SymbolTable());

        //ProcTable (global shared)
        IProcTable procTable = new ProcTable();

        try {
            procTable.add("sum", List.of("a", "b"), view.ExamplePrograms.buildSumBody());
            procTable.add("product", List.of("a", "b"), view.ExamplePrograms.buildProductBody());
        } catch (exceptions.MyException e) {
            e.printStackTrace();
        }

        //push program ONCE

        exeStack.push(program);

        programState = new ProgramState(
                exeStack,
                symTableStack,
                out,
                fileTable,
                heap,
                lockTable,
                procTable,
                barrierTable,
                program
        );


        repo = new Repository("GUI_LOGFILE.txt");
        repo.addProgram(programState);
        controller = new GUIController(repo);

        // --- UI COMPONENTS ---
        nrPrgStatesField = new TextField("1");
        nrPrgStatesField.setEditable(false);

        // Heap Table
        heapTable = new TableView<>();
        TableColumn<HeapEntry, Integer> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<HeapEntry, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        heapTable.getColumns().addAll(addressCol, valueCol);

        // SymTable Table
        symTable = new TableView<>();
        TableColumn<SymTableEntry, String> varNameCol = new TableColumn<>("Variable");
        varNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SymTableEntry, String> varValueCol = new TableColumn<>("Value");
        varValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        symTable.getColumns().addAll(varNameCol, varValueCol);

        barrierTableView = new TableView<> ();

        TableColumn<BarrierEntryRow,Integer> bIndexCol = new TableColumn<>("Barrier");
        bIndexCol.setCellValueFactory(new PropertyValueFactory<>("index"));

        TableColumn<BarrierEntryRow, Integer> bValueCol = new TableColumn<>("Value");
        bValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        TableColumn<BarrierEntryRow, String> bListCol = new TableColumn<>("List");
        bListCol.setCellValueFactory(new PropertyValueFactory<>("list"));

        barrierTableView.getColumns().addAll(bIndexCol, bValueCol, bListCol);



        // ListViews
        outList = new ListView<>();
        fileTableList = new ListView<>();
        prgStateIdsList = new ListView<>();
        exeStackList = new ListView<>();

        // Run One Step Button
        runOneStepButton = new Button("Run one step");
        runOneStepButton.setOnAction(e -> runOneStep());

        // --- Layout ---
        HBox topBox = new HBox(10, new Label("Number of PrgStates:"), nrPrgStatesField, runOneStepButton);
        topBox.setPadding(new Insets(10));

        VBox leftBox = new VBox(10,
                new Label("PrgState IDs"), prgStateIdsList,
                new Label("Heap"), heapTable,
                new Label("BarrierTable") , barrierTableView,
                new Label("Out"), outList,
                new Label("FileTable"), fileTableList
        );
        leftBox.setPadding(new Insets(10));

        VBox rightBox = new VBox(10,
                new Label("SymTable"), symTable,
                new Label("ExeStack"), exeStackList
        );
        rightBox.setPadding(new Insets(10));

        HBox mainBox = new HBox(10, leftBox, rightBox);

        VBox root = new VBox(10, topBox, mainBox);
        Scene scene = new Scene(root, 1000, 600);
        root.setStyle("-fx-background-color: pink;");

        // --- Event: selecting a program state updates symTable and exeStack ---
        prgStateIdsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) updateSymTableAndStack(newSelection);
        });

        // --- Initial update ---
        updateAllViews();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- Updates all views ---
    private void updateAllViews() {
        List<ProgramState> prgStates = repo.getProgramList();
        nrPrgStatesField.setText(String.valueOf(prgStates.size()));

        // Update PrgState IDs
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        for (ProgramState ps : prgStates) ids.add(ps.id());
        prgStateIdsList.setItems(ids);

        // Default select first
        if (!ids.isEmpty()) prgStateIdsList.getSelectionModel().selectFirst();

        // Update Heap
        IHeap heap = programState.heap();
        ObservableList<HeapEntry> heapEntries = FXCollections.observableArrayList();
        for (Map.Entry<Integer, Value> e : heap.getContent().entrySet()) {
            heapEntries.add(new HeapEntry(e.getKey(), e.getValue().toString()));
        }
        heapTable.setItems(heapEntries);

        var bt = programState.barrierTable();
        ObservableList<BarrierEntryRow> barrierRows = FXCollections.observableArrayList();

        for(Map.Entry<Integer,BarrierEntry> e : bt.getContent().entrySet()){
            int idx = e.getKey();
            int required = e.getValue().getRequired();
            String list = e.getValue().getWaiting().toString();
            barrierRows.add(new BarrierEntryRow(idx,required,list));
        }
        barrierTableView.setItems(barrierRows);

        // Update Out
        IList<Value> out = programState.out();
        ObservableList<String> outItems = FXCollections.observableArrayList();
        // Manually extract from IList
        if (out instanceof model.ADT.List.ListOut<?> listOut) {
            for (Object v : ((model.ADT.List.ListOut<?>) out).getInternalList()) {
                outItems.add(v.toString());
            }
        }
        outList.setItems(outItems);

        // Update FileTable
        IFileTable fileTable = programState.fileTable();
        ObservableList<String> fileItems = FXCollections.observableArrayList();
        for (Value key : fileTable.getContent().keySet()) {
            fileItems.add(key.toString());
        }
        fileTableList.setItems(fileItems);

        // SymTable and ExeStack handled by selection listener
        Integer selectedId = prgStateIdsList.getSelectionModel().getSelectedItem();
        if (selectedId != null) updateSymTableAndStack(selectedId);
    }

    private void updateSymTableAndStack(int prgId) {
        ProgramState ps = repo.getProgramList().stream().filter(p -> p.id() == prgId).findFirst().orElse(null);
        if (ps == null) return;

        // SymTable
        IMap<String, Value> sym = ps.symbolTable();
        ObservableList<SymTableEntry> symEntries = FXCollections.observableArrayList();
        for (Map.Entry<String, Value> e : sym.getContent().entrySet()) {
            symEntries.add(new SymTableEntry(e.getKey(), e.getValue().toString()));
        }
        symTable.setItems(symEntries);

        // ExeStack (top first)
        IStack<Statement> exeStack = ps.executionStack();
        ObservableList<String> stackItems = FXCollections.observableArrayList();
        if (exeStack instanceof model.ADT.Stack.StackExecutionStack<?> stackImpl) {
            List<?> reversed = new ArrayList<>(stackImpl.getInternalList());
            for (int i = reversed.size() - 1; i >= 0; i--) {
                stackItems.add(reversed.get(i).toString());
            }
        }
        exeStackList.setItems(stackItems);
    }

    // --- Run one step ---
    private void runOneStep() {
        try {
            controller.oneStepForAllPrg(repo.getProgramList());
            updateAllViews();
        } catch (MyException e) {
            // program finished
            runOneStepButton.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage());
            alert.showAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    // --- Helper classes for TableView ---
    public static class HeapEntry {
        private final Integer address;
        private final String value;

        public HeapEntry(Integer address, String value) {
            this.address = address;
            this.value = value;
        }

        public Integer getAddress() { return address; }
        public String getValue() { return value; }
    }

    public static class SymTableEntry {
        private final String name;
        private final String value;

        public SymTableEntry(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public String getValue() { return value; }
    }
}