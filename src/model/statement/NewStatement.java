package model.statement;

import exceptions.MyException;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public class NewStatement implements Statement {

    private final String varName;
    private final Expression expression;

    public NewStatement(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMap<String, Value> symTable = state.symbolTable();
        IHeap heap = state.heap();

        //1. var must exist
        if (!symTable.isDefined(varName)) {
            throw new MyException("Variable " + varName + " is not defined");
        }

        Value varValue = symTable.getValue(varName);

        //2. Check if variable is of RefType
        if (!(varValue.getType() instanceof RefType)) {
            throw new MyException("Variable " + varName + " is not of RefType");
        }

        RefType refType = (RefType) varValue.getType();

        //3. evaluate the expression

        Value evaluatedValue = expression.evaluate(symTable, heap);

        //4. Type check: value type must match the RefType's location type
        if (!evaluatedValue.getType().equals(refType.getInner())) {
            throw new MyException(
                    "Type of expression " + evaluatedValue.getType() +
                            " does not match location type " + refType.getInner()
            );}


            //5.allocate in heap

            int newAddress = heap.allocate(evaluatedValue); // allocate returns the new key/address

            //6.update symbol table with new RefValue
            RefValue newRefValue = new RefValue(newAddress, refType.getInner());
            symTable.update(varName, newRefValue);

//
        return null;
        }

        @Override
    public Statement deepCopy() {
            return new NewStatement(varName, expression.deepCopy());
    }
    }
