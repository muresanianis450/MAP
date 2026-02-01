package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.expression.Expression;
import model.expression.NotExpression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.Type;

public class RepeatUntilStatement implements Statement {

    private final Statement stmt1;
    private final Expression exp2;

    public RepeatUntilStatement(Statement stmt1, Expression exp2) {
        this.stmt1 = stmt1;
        this.exp2 = exp2;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        // repeat S until E
        // becomes:
        // S;
        // while(!E) S;

        Statement desugared = new CompoundStatement(
                stmt1,
                new WhileStatement(
                        new NotExpression(exp2),
                        stmt1.deepCopy()
                )
        );

        state.executionStack().push(desugared);
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new RepeatUntilStatement(stmt1.deepCopy(), exp2.deepCopy());
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {

        // exp2 must be bool
        Type condType = exp2.typeCheck(typeEnv);
        if (!condType.equals(new BooleanType())) {
            throw new MyException("RepeatUntil: condition is not boolean");
        }

        // typecheck stmt1
        stmt1.typeCheck(typeEnv.deepCopy());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "repeat(" + stmt1 + ") until(" + exp2 + ")";
    }
}
