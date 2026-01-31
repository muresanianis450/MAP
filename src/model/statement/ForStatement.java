package model.statement;


import exceptions.MyException;
import model.ADT.Map.IMap;
import model.expression.Expression;
import model.expression.RelationalExpression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;

/**
 * for ( v = exp1; v < exp2 ; v = exp3) stmt
 * This is how we are going to sugarcoat a for loop:
 * int v;
 * v = exp1;
 * while ( v < exp2 ) (stms; v= = exp3)
 */


public class ForStatement implements Statement {

    private final String variableName;
    private final Expression exp1;
    private final Expression exp2;
    private final Expression exp3;
    private final Statement statement;


    public ForStatement(String variableName, Expression exp1, Expression exp2, Expression exp3, Statement statement) {
        this.variableName = variableName;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        // for becomes while

        Statement desugared =
                new CompoundStatement(
                        new VariableDeclarationStatement(variableName, new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement(variableName, exp1),
                                new WhileStatement(
                                        new RelationalExpression(
                                                new model.expression.VariableExpression(variableName),
                                                exp2,
                                                "<"
                                        ),
                                        new CompoundStatement(
                                                statement,
                                                new AssignmentStatement(variableName, exp3)
                                        )
                                )
                        )
                );


        state.executionStack().push(desugared);
        return null;
    }
    @Override
    public Statement deepCopy(){
        return new ForStatement(variableName, exp1.deepCopy(), exp2.deepCopy(), exp3.deepCopy(), statement.deepCopy());
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {



        // exp1 must be int (doesn't need v in scope)
        Type t1 = exp1.typeCheck(typeEnv);
        if (!t1.equals(new IntegerType()))
            throw new MyException("FOR: exp1 is not int");

        // In the FOR, v exists after initialization. So for exp2 and exp3, v must be visible.
        IMap<String, Type> envWithV = typeEnv.deepCopy();

        if (envWithV.isDefined(variableName))
            throw new MyException("FOR: variable already declared: " + variableName);

        envWithV.add(variableName, new IntegerType());




        // exp2 and exp3 must be int (NOW v is visible)
        Type t2 = exp2.typeCheck(envWithV);
        if (!t2.equals(new IntegerType()))
            throw new MyException("FOR: exp2 is not int");

        Type t3 = exp3.typeCheck(envWithV);
        if (!t3.equals(new IntegerType()))
            throw new MyException("FOR: exp3 is not int");

        // Now typecheck the desugared statement using envWithV’s *base* rules
        Statement desugared =
                new CompoundStatement(
                        new VariableDeclarationStatement(variableName, new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement(variableName, exp1),
                                new WhileStatement(
                                        new RelationalExpression(
                                                new model.expression.VariableExpression(variableName),
                                                exp2,
                                                "<"
                                        ),
                                        new CompoundStatement(
                                                statement,
                                                new AssignmentStatement(variableName, exp3)
                                        )
                                )
                        )
                );

        desugared.typeCheck(typeEnv.deepCopy());


        return typeEnv;
    }

    @Override
    public String toString(){
        return "for(" + variableName + "=" + exp1 + "; " + variableName + "<" + exp2 + "; " +
                variableName + "=" + exp3 + ") " + statement;
    }
}
