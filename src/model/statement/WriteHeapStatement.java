package model.statement;

import exceptions.MyException;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.RefType;
import model.value.RefValue;
import model.value.Value;

public class WriteHeapStatement implements Statement{

    private final String varName;
    private final Expression expression;

    public WriteHeapStatement(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMap<String,Value> symTable = state.symbolTable();
        IHeap heap = state.heap();

        //check if variable exists
        if(!symTable.isDefined(varName)){
            throw new MyException("Variable " + varName + " is not defined");
        }

        Value varValue = symTable.getValue(varName);

        //check if variable is of RefType

        if(!(varValue.getType() instanceof RefType)){
            throw new MyException("Variable " + varName + " is not of RefType");
        }
        RefValue refValue = (RefValue) varValue;
        int address = refValue.getAddress();

        //check if address exists in heap
        if (!heap.isDefined(address)) {
            throw new MyException("Address " + address + " is not defined in the heap");
        }

        //evaluate expression
        Value evaluateValue = expression.evaluate(symTable,heap);

        //type check expression type must match the location type
        if(!evaluateValue.getType().equals(refValue.getLocationType())){
            throw new MyException("Type of expression " + evaluateValue.getType() + " does not match location type " + refValue.getLocationType());
        }
        //update heap
        heap.update(address, evaluateValue);
        return state;
    }
    @Override
    public String toString(){
        return "wH(" + varName + ", " + expression.toString() + ")";
    }
}