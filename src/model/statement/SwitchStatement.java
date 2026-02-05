package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.expression.Expression;
import model.expression.RelationalExpression;
import model.type.Type;
public class SwitchStatement implements Statement{

    private final Expression exp;
    private final Expression exp1;
    private final Statement stmt1;
    private final Expression exp2;
    private final Statement stmt2;
    private final Statement defaultStmt;


    public SwitchStatement(Expression exp,
                           Expression exp1,
                           Statement stmt1,
                           Expression exp2,
                           Statement stmt2,
                           Statement defaultStmt){
        this.exp = exp;
        this.exp1 = exp1;
        this.stmt1 = stmt1;
        this.exp2 = exp2;
        this.stmt2 = stmt2;
        this.defaultStmt = defaultStmt;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        //switch(exp) case exp1: stmt1 ; case exp2: stmt2 : stmt2 default : stmtDefault
        //desugars to
        /*if ( exp == exp1)
        *   stmt1
        * else
        * (if (exp == exp2)
        *   stmt2
        * else stmt3)
        */
        //basically a switch statement is just a syntactic sugar for nested if statements
        Statement desugared =
                    new IfStatement(
                            new RelationalExpression(exp,exp1,"=="),
                            stmt1,
                            new IfStatement(
                                    new RelationalExpression(exp,exp2,"=="),
                                    stmt2,
                                    defaultStmt
                            )
                    );
        state.executionStack().push(desugared);
        return null ;
    }

    @Override
    public Statement deepCopy(){
        return new SwitchStatement(exp.deepCopy(), exp1.deepCopy(), stmt1.deepCopy(),
                                   exp2.deepCopy(), stmt2.deepCopy(), defaultStmt.deepCopy());
    }

    @Override
    public String toString(){
        return "switch(" + exp + ") " +
                "(case " + exp1 + ": " + stmt1 + ") " +
                "(case " + exp2 + ": " + stmt2 + ") " +
                "(default: " + defaultStmt + ")";
    }

    @Override
    public IMap<String,Type> typeCheck(IMap<String,Type> typeEnv) throws MyException {
        Type tExp = exp.typeCheck(typeEnv);
        Type t1 = exp1.typeCheck(typeEnv);
        Type t2 = exp2.typeCheck(typeEnv);

        if(!tExp.equals(t1))
            throw new MyException("SWITCH: exp and exp1 have different types");
        if(!tExp.equals(t2))
            throw new MyException("SWITCH: exp and exp2 have different types");

        stmt1.typeCheck(typeEnv.deepCopy());
        stmt2.typeCheck(typeEnv.deepCopy());
        defaultStmt.typeCheck(typeEnv.deepCopy());

        return typeEnv;
    }
}
