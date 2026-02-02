package model.statement;

import model.ADT.Map.IMap;
import model.ADT.Map.SymbolTable;
import model.ADT.Stack.IStack;
import model.ADT.Stack.StackExecutionStack;
import model.state.ProgramState;
import model.type.Type;
import model.value.Value;
import exceptions.MyException;

import java.util.List;
import model.ADT.BarrierTable.IBarrierTable;
public class ForkStatement implements Statement {
    private final Statement forkedStatement;

    public ForkStatement(Statement forkedStatement) {
        this.forkedStatement = forkedStatement;
    }

    @Override
    public ProgramState execute(ProgramState parentState) throws MyException {
        //create a new execution stack for the forked thread
        IStack<Statement> newStack = new StackExecutionStack<>();
        newStack.push(forkedStatement.deepCopy());

        /*
        //clone the parent symbol table (so variables are scoped)
        IMap<String, Value> newSymbolTable = parentState.symbolTable().deepCopy();
*/

        //FOR SAFETY WE COPY THE SYMBOL TABLE MANUALLY

        /* IMap<String, Value> newSymbolTable = new SymbolTable<>();
        for (var entry : parentState.symbolTable().getContent().entrySet()) {
            newSymbolTable.add(entry.getKey(), entry.getValue().deepCopy());
        }*/

        IStack<IMap<String,Value>> newSymStack = new StackExecutionStack<>();
        List<IMap<String,Value>> topFirst = parentState.symTableStack().getReversed();
        //top first : [TOP, ... , BOTTOM] because internal list is top-first (LinkedList)
        for(int i = topFirst.size() - 1 ; i >= 0 ; i--){
            newSymStack.push(deepCopySymTable(topFirst.get(i)));
        }


        //heap, Out, FileTable are shared
        return new ProgramState(
                newStack,
                newSymStack,
                parentState.out(),
                parentState.fileTable(),
                parentState.heap(),
                parentState.lockTable(),
                parentState.procTable(),
                parentState.barrierTable(),
                parentState.latchTable(),
                parentState.semaphoreTable(),
                forkedStatement
        );

    }

    @Override
    public Statement deepCopy() {
        return new ForkStatement(forkedStatement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + forkedStatement + ")";
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        forkedStatement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    //we need to clone all the stack for proc
    private IMap<String,Value> deepCopySymTable (IMap<String,Value> st){
        IMap<String,Value> copy = new SymbolTable<>();
        for( var e : st.getContent().entrySet() ){
            copy.add(e.getKey(), e.getValue().deepCopy());
        }
        return copy;
    }
}
