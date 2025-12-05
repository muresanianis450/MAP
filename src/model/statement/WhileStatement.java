package model.statement;


import exceptions.MyException;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.expression.Expression;
import model.state.ProgramState;
import model.value.BooleanValue;
import model.value.Value;


public class WhileStatement implements Statement {
    private final Expression condition;
    private final Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        IMap<String,Value> symTable = state.symbolTable();
        IHeap heap = state.heap();

        //Evaluate the condition
        Value condValue = condition.evaluate(symTable,heap);

        if(!(condValue instanceof BooleanValue)){
            throw new MyException("Condition evaluation failed");
        }

        if(((BooleanValue) condValue).getValue()){
            //If true,push the body and the while statement ifself ( of next iteration)
            state.executionStack().push(this);
            state.executionStack().push(body);
        }
        //else do nothing -> the loop ends

        return null;

    }

    @Override
    public String toString(){
        return "while(" + condition.toString()+")" + body.toString();
    }

    @Override
    public Statement deepCopy(){
        return new WhileStatement(condition.deepCopy(), body.deepCopy());
    }
}


