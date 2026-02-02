package gui;

import controller.GUIController;
import exceptions.MyException;
import model.ADT.BarrierTable.BarrierEntry;
import model.ADT.BarrierTable.BarrierTable;
import model.ADT.BarrierTable.IBarrierTable;
import model.ADT.FileTable.IFileTable;
import model.ADT.Heap.IHeap;
import model.ADT.LatchTable.ILatchTable;
import model.ADT.LatchTable.LatchTable;
import model.ADT.List.IList;
import model.ADT.LockTable.ILockTable;
import model.ADT.Map.IMap;
import model.ADT.Map.SymbolTable;
import model.ADT.ProcTable.IProcTable;
import model.ADT.ProcTable.ProcTable;
import model.ADT.SemaphoreTable.ISemaphoreTable;
import model.ADT.Stack.IStack;
import model.ADT.SemaphoreTable.SemaphoreEntry;
import model.state.ProgramState;
import model.statement.Statement;
import model.value.Value;
import repository.Repository;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainWindow extends Application {

    private final Statement program; // Selected program
    private GUIController controller;
    private ProgramState programState;
    private Repository repo;

    // --- JavaFX UI components ---
    private TextField nrPrgStatesField;
    private Button runOneStepButton;

    private TableView<HeapEntry> heapTable;
    private TableView<SymTableEntry> symTable;
    private TableView<BarrierEntryRow> barrierTableView;
    private TableView<LatchRow> latchTableView;

    private ListView<String> outList;
    private ListView<String> fileTableList;
    private ListView<String> exeStackList;
    private ListView<Integer> prgStateIdsList;
    private TableView<SemaphoreRow> semaphoreTableView;

    public MainWindow(Statement program) {
        this.program = program;
    }

    // -------------------- ROW CLASSES --------------------
    public static class BarrierEntryRow {
        private final Integer index;
        private final Integer value;
        private final String list;

        public BarrierEntryRow(Integer index, Integer value, String list) {
            this.index = index;
            this.value = value;
            this.list = list;
        }

        public Integer getIndex() { return index; }
        public Integer getValue() { return value; }
        public String getList() { return list; }
    }

    public static class LatchRow {
        private final Integer location;
        private final Integer value;

        public LatchRow(Integer location, Integer value) {
            this.location = location;
            this.value = value;
        }

        public Integer getLocation() { return location; }
        public Integer getValue() { return value; }
    }

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

    public static class SemaphoreRow {
        private final Integer index;
        private final Integer value;
        private final String list;

        public SemaphoreRow(Integer index, Integer value, String list) {
            this.index = index;
            this.value = value;
            this.list = list;
        }

        public Integer getIndex() { return index; }
        public Integer getValue() { return value; }
        public String getList() { return list; }
    }
    // -------------------- START --------------------
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Interpreter GUI - Main Window");

        // --- Initialize ProgramState, Repository, Controller ---
        IStack<Statement> exeStack = new model.ADT.Stack.StackExecutionStack<>();
        IList<Value> out = new model.ADT.List.ListOut<>();
        IHeap heap = new model.ADT.Heap.Heap();
        IFileTable fileTable = new model.ADT.FileTable.FileTable();
        ILockTable lockTable = new model.ADT.LockTable.LockTable();
        IBarrierTable barrierTable = new BarrierTable();
        ILatchTable latchTable = new LatchTable();

        // Procedures: SymTable STACK
        IStack<IMap<String, Value>> symTableStack = new model.ADT.Stack.StackExecutionStack<>();
        symTableStack.push(new SymbolTable<>());

        // ProcTable (global shared)
        IProcTable procTable = new ProcTable();
        try {
            procTable.add("sum", List.of("a", "b"), view.ExamplePrograms.buildSumBody());
            procTable.add("product", List.of("a", "b"), view.ExamplePrograms.buildProductBody());
        } catch (exceptions.MyException e) {
            e.printStackTrace();
        }
        ISemaphoreTable semaphoreTable = new model.ADT.SemaphoreTable.SemaphoreTable();


        // push program ONCE
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
                latchTable,
                semaphoreTable,
                program
        );

        repo = new Repository("GUI_LOGFILE.txt");
        repo.addProgram(programState);
        controller = new GUIController(repo);

        // --- Build UI components ---
        buildControls();

        // --- Layout ---
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: pink;");

        root.setTop(buildTopBar());

        // Left: Program IDs
        VBox leftPane = new VBox(8,
                sectionLabel("PrgState IDs"),
                prgStateIdsList
        );
        leftPane.setPadding(new Insets(6));
        leftPane.setPrefWidth(160);

        // Center: Tables (scrollable)
        VBox tablesBox = new VBox(10,
                section("Heap", heapTable, 200),
                section("SymTable (selected PrgState)", symTable, 200),
                section("BarrierTable", barrierTableView, 160),
                section("LatchTable", latchTableView, 160),
                section("SemaphoreTable", semaphoreTableView, 160)
        );
        tablesBox.setPadding(new Insets(6));
        ScrollPane tablesScroll = new ScrollPane(tablesBox);
        tablesScroll.setFitToWidth(true);
        tablesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tablesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Right: Runtime views (scrollable)
        VBox runtimeBox = new VBox(10,
                section("ExeStack (selected PrgState)", exeStackList, 260),
                section("Out", outList, 160),
                section("FileTable", fileTableList, 120)
        );
        runtimeBox.setPadding(new Insets(6));
        ScrollPane runtimeScroll = new ScrollPane(runtimeBox);
        runtimeScroll.setFitToWidth(true);
        runtimeScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        runtimeScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Put center + right with a SplitPane so you can resize
        SplitPane split = new SplitPane(tablesScroll, runtimeScroll);
        split.setOrientation(Orientation.HORIZONTAL);
        split.setDividerPositions(0.62);

        root.setLeft(leftPane);
        root.setCenter(split);

        // --- Event: selecting a program state updates symTable and exeStack ---
        prgStateIdsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) updateSymTableAndStack(newSelection);
        });

        // --- Initial update ---
        updateAllViews();

        Scene scene = new Scene(root, 1200, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void buildControls() {
        nrPrgStatesField = new TextField("1");
        nrPrgStatesField.setEditable(false);
        nrPrgStatesField.setPrefWidth(80);

        runOneStepButton = new Button("Run one step");
        runOneStepButton.setOnAction(e -> runOneStep());

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

        // BarrierTable Table
        barrierTableView = new TableView<>();
        TableColumn<BarrierEntryRow, Integer> bIndexCol = new TableColumn<>("Index");
        bIndexCol.setCellValueFactory(new PropertyValueFactory<>("index"));
        TableColumn<BarrierEntryRow, Integer> bValueCol = new TableColumn<>("Value");
        bValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        TableColumn<BarrierEntryRow, String> bListCol = new TableColumn<>("Waiting IDs");
        bListCol.setCellValueFactory(new PropertyValueFactory<>("list"));
        barrierTableView.getColumns().addAll(bIndexCol, bValueCol, bListCol);

        // LatchTable Table
        latchTableView = new TableView<>();
        TableColumn<LatchRow, Integer> lLocCol = new TableColumn<>("Location");
        lLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        TableColumn<LatchRow, Integer> lValCol = new TableColumn<>("Value");
        lValCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        latchTableView.getColumns().addAll(lLocCol, lValCol);


        //Semaphore Table
        semaphoreTableView = new TableView<>();
        TableColumn<SemaphoreRow, Integer> sIndexCol = new TableColumn<>("Index");
        sIndexCol.setCellValueFactory(new PropertyValueFactory<>("index"));
        TableColumn<SemaphoreRow, Integer> sValueCol = new TableColumn<>("Value");
        sValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        TableColumn<SemaphoreRow, String> sListCol = new TableColumn<>("Acquired IDs");
        sListCol.setCellValueFactory(new PropertyValueFactory<>("list"));
        semaphoreTableView.getColumns().addAll(sIndexCol, sValueCol, sListCol);


        // Lists
        outList = new ListView<>();
        fileTableList = new ListView<>();
        prgStateIdsList = new ListView<>();
        exeStackList = new ListView<>();
    }

    private HBox buildTopBar() {
        HBox top = new HBox(10,
                new Label("Number of PrgStates:"),
                nrPrgStatesField,
                runOneStepButton
        );
        top.setPadding(new Insets(6));
        top.setStyle("-fx-background-color: rgba(255,255,255,0.4); -fx-background-radius: 10;");
        return top;
    }

    // -------------------- UPDATE VIEWS --------------------
    private void updateAllViews() {
        List<ProgramState> prgStates = repo.getProgramList();
        nrPrgStatesField.setText(String.valueOf(prgStates.size()));

        // Update PrgState IDs
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        for (ProgramState ps : prgStates) ids.add(ps.id());
        prgStateIdsList.setItems(ids);

        // Default select first
        if (!ids.isEmpty() && prgStateIdsList.getSelectionModel().getSelectedItem() == null) {
            prgStateIdsList.getSelectionModel().selectFirst();
        }

        // Update Heap (global/shared)
        ObservableList<HeapEntry> heapEntries = FXCollections.observableArrayList();
        for (Map.Entry<Integer, Value> e : programState.heap().getContent().entrySet()) {
            heapEntries.add(new HeapEntry(e.getKey(), e.getValue().toString()));
        }
        heapTable.setItems(heapEntries);

        // Update BarrierTable (global/shared)
        ObservableList<BarrierEntryRow> barrierRows = FXCollections.observableArrayList();
        var bt = programState.barrierTable();
        for (Map.Entry<Integer, BarrierEntry> e : bt.getContent().entrySet()) {
            barrierRows.add(new BarrierEntryRow(
                    e.getKey(),
                    e.getValue().getRequired(),
                    e.getValue().getWaiting().toString()
            ));
        }
        barrierTableView.setItems(barrierRows);

        // Update LatchTable (global/shared)
        ObservableList<LatchRow> latchRows = FXCollections.observableArrayList();
        var lt = programState.latchTable();
        for (Map.Entry<Integer, Integer> e : lt.getContent().entrySet()) {
            latchRows.add(new LatchRow(e.getKey(), e.getValue()));
        }
        latchTableView.setItems(latchRows);

        // Update Out (global/shared)
        ObservableList<String> outItems = FXCollections.observableArrayList();
        IList<Value> out = programState.out();
        if (out instanceof model.ADT.List.ListOut<?> listOut) {
            for (Object v : ((model.ADT.List.ListOut<?>) out).getInternalList()) {
                outItems.add(v.toString());
            }
        }
        outList.setItems(outItems);

        // Update FileTable (global/shared)
        ObservableList<String> fileItems = FXCollections.observableArrayList();
        IFileTable fileTable = programState.fileTable();
        for (Value key : fileTable.getContent().keySet()) {
            fileItems.add(key.toString());
        }
        fileTableList.setItems(fileItems);

        //Update SemaphoreTable (global/shared)
        ObservableList<SemaphoreRow> semRows = FXCollections.observableArrayList();
        var st = programState.semaphoreTable();
        for (Map.Entry<Integer, SemaphoreEntry> e : st.getContent().entrySet()) {
            int index = e.getKey();
            SemaphoreEntry entry = e.getValue();
            semRows.add(new SemaphoreRow(
                    index,
                    entry.getPermits(),
                    entry.getAcquiredBy().toString()
            ));
        }
        semaphoreTableView.setItems(semRows);

        // Update per-selected program state views
        Integer selectedId = prgStateIdsList.getSelectionModel().getSelectedItem();
        if (selectedId != null) updateSymTableAndStack(selectedId);
    }

    private void updateSymTableAndStack(int prgId) {
        ProgramState ps = repo.getProgramList().stream().filter(p -> p.id() == prgId).findFirst().orElse(null);
        if (ps == null) return;

        // SymTable
        ObservableList<SymTableEntry> symEntries = FXCollections.observableArrayList();
        IMap<String, Value> sym = ps.symbolTable();
        for (Map.Entry<String, Value> e : sym.getContent().entrySet()) {
            symEntries.add(new SymTableEntry(e.getKey(), e.getValue().toString()));
        }
        symTable.setItems(symEntries);

        // ExeStack (top first)
        ObservableList<String> stackItems = FXCollections.observableArrayList();
        IStack<Statement> exeStack = ps.executionStack();
        if (exeStack instanceof model.ADT.Stack.StackExecutionStack<?> stackImpl) {
            List<?> internal = new ArrayList<>(stackImpl.getInternalList());
            for (int i = internal.size() - 1; i >= 0; i--) {
                stackItems.add(internal.get(i).toString());
            }
        }
        exeStackList.setItems(stackItems);
    }

    // -------------------- RUN ONE STEP --------------------
    private void runOneStep() {
        try {
            controller.oneStepForAllPrg(repo.getProgramList());
            updateAllViews();
        } catch (MyException e) {
            runOneStepButton.setDisable(true);
            new Alert(Alert.AlertType.INFORMATION, e.getMessage()).showAndWait();
        } catch (InterruptedException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    // -------------------- UI HELPERS --------------------
    private Label sectionLabel(String title) {
        Label label = new Label(title);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #222;");
        return label;
    }

    private VBox section(String title, Control control, double prefHeight) {
        Label label = sectionLabel(title);

        if (control instanceof TableView<?> tv) {
            tv.setPrefHeight(prefHeight);
            tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        } else if (control instanceof ListView<?> lv) {
            lv.setPrefHeight(prefHeight);
        }

        VBox box = new VBox(6, label, control);
        box.setPadding(new Insets(6));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.45); -fx-background-radius: 10;");
        return box;
    }
}
