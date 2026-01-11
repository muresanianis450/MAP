package model.expression;

import exceptions.MyException;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.type.Type;
import model.value.Value;
import model.value.RefValue;
import model.expression.Expression;
import model.type.RefType;

public class ReadHeapExpression implements Expression{
    private final Expression expression;

    public ReadHeapExpression(Expression expression){
        this.expression = expression;
    }

    @Override
    public Value evaluate(IMap<String, Value> symTable, IHeap heap) throws MyException {
        //evaluate inner expression
        Value value = expression.evaluate(symTable, heap);

        //check that it is a RefValue
        if (!(value instanceof RefValue))
            throw new MyException("ReadHeapExpression: expression is not a RefValue");

        RefValue refValue = (RefValue) value;
        int address = refValue.getAddress();

        //read from heap
        if (!heap.isDefined(address))
            throw new MyException("ReadHeapExpression: address " + address + " is not defined in the heap");

        if(address == -1 )
            throw new MyException("Cannot read from uninitialized reference.");

        return heap.get(address);
    }

    @Override
    public String toString(){
        return "rH(" + expression.toString() + ")";
    }

    @Override
    public Expression deepCopy() {
        return new ReadHeapExpression(this.expression.deepCopy());
    }

    @Override
    public Type typeCheck(IMap<String, Type> typeEnv) throws MyException {
        // Type-check the inner expression
        Type innerType = expression.typeCheck(typeEnv);

        // The inner expression must be a RefType
        if (!(innerType instanceof RefType refType)) {
            throw new MyException("ReadHeapExpression: expression is not of RefType");
        }

        // Return the type stored in the reference
        return refType.getInner();
    }

}
