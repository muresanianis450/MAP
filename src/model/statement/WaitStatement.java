package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.expression.ConstantExpression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;

public class WaitStatement implements Statement {
    private final int number;

    public WaitStatement(int number) {
        this.number = number;
    }

    @Override
    public ProgramState execute(model.state.ProgramState state) throws MyException {
        // pop is already done by ProgramState.oneStep()

        if (number < 0) {
            throw new MyException("wait: number must be >= 0");
        }

        if (number == 0) {
            // do nothing
            return null;
        }

        // push (print(number); wait(number-1)) on the stack
        // IMPORTANT: push in reverse order because stack is LIFO:
        // if we push compound directly, that's easiest.

        Statement next =
                new CompoundStatement(
                        new PrintStatement(new ConstantExpression(new IntegerValue(number))),
                        new WaitStatement(number - 1)
                );

        state.executionStack().push(next);
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new WaitStatement(number);
    }

    @Override
    public String toString() {
        return "wait(" + number + ")";
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        // wait(number) has a literal number in your requirement, no variable/exp.
        // So typecheck is always ok; keep it clean.
        return typeEnv;
    }
}
