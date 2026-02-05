package model.statement;

import exceptions.MyException;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.ADT.SemaphoreTable.ISemaphoreTable;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class CreateSemaphoreStatement implements Statement {
    private final String var;
    private final Expression exp1;

    public CreateSemaphoreStatement(String var, Expression exp1) {
        this.var = var;
        this.exp1 = exp1;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        //acquire the symbol table from the current state
        IMap<String, Value> symTable = state.symbolTable();
        //acquire the heap table from the current state
        IHeap heap = state.heap();

        //acquire the semaphore table from the current state
        ISemaphoreTable semTable = state.semaphoreTable();

        // evaluate exp1
        Value number1Val = exp1.evaluate(symTable, heap);
        if (!(number1Val instanceof IntegerValue)) {
            throw new MyException("createSemaphore: exp1 is not int");
        }

        int number1 = ((IntegerValue) number1Val).getValue();

        // add new semaphore entry atomically (allocate is synchronized)
        int newFreeLocation = semTable.allocate(number1);

        // var must exist and be int
        if (!symTable.isDefined(var)) {
            throw new MyException("createSemaphore: variable " + var + " is not defined");
        }

        Value varVal = symTable.lookup(var);
        if (!varVal.getType().equals(new IntegerType())) {
            throw new MyException("createSemaphore: variable " + var + " is not int");
        }

        // update var with new free location
        symTable.update(var, new IntegerValue(newFreeLocation));

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new CreateSemaphoreStatement(var, exp1.deepCopy());
    }

    @Override
    public String toString() {
        return "createSemaphore(" + var + ", " + exp1 + ")";
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        if (!typeEnv.isDefined(var)) {
            throw new MyException("createSemaphore: variable not declared: " + var);
        }
        Type varType = typeEnv.lookup(var);
        Type expType = exp1.typeCheck(typeEnv);

        if (!varType.equals(new IntegerType())) {
            throw new MyException("createSemaphore: var is not int");
        }
        if (!expType.equals(new IntegerType())) {
            throw new MyException("createSemaphore: exp1 is not int");
        }

        return typeEnv;
    }
}
