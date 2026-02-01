package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.Type;



public class ConditionalAssignmentStatement  implements Statement{

    private final String varName;
    private final Expression exp1;
    private final Expression exp2;
    private final Expression exp3;


    // v = exp1 ? exp2 : exp3

    public ConditionalAssignmentStatement(String varName, Expression exp1, Expression exp2, Expression exp3) {
        this.varName = varName;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    @Override
    public ProgramState execute (ProgramState state) throws MyException {
        /*spec:
        *- pop statement ( already popped in ProgramState.oneStep()
        *- create: if (exp1) then v = exp2 else v = exp3
        * - push the new statement on stack
        * */

        Statement desugared =
                new IfStatement(
                        exp1,
                        new AssignmentStatement(varName, exp2),
                        new AssignmentStatement(varName, exp3)
                );

        state.executionStack().push(desugared);
        return null;
    }

    @Override
    public Statement deepCopy(){
        return new ConditionalAssignmentStatement(varName, exp1.deepCopy(), exp2.deepCopy(), exp3.deepCopy());
    }

    @Override
    public IMap<String,Type> typeCheck(IMap<String,Type> typeEnv) throws MyException {
        // exp1 must be bool

        Type t1 = exp1.typeCheck(typeEnv);
        if (!t1.equals(new BooleanType())){
            throw new MyException("ConditionalAssignment: exp1 is not bool");
        }

        //v, exp2, exp3 must have the same type

        if(!typeEnv.isDefined(varName)){
            throw new MyException("ConditionalAssignment: variable not declared: " + varName);
        }

        Type varType = typeEnv.lookup(varName);
        Type t2 = exp2.typeCheck(typeEnv);
        Type t3 = exp3.typeCheck(typeEnv);

        if(!varType.equals(t2)){
            throw new MyException("ConditionalAssignment: variable and exp2 have different types");
        }
        if(!varType.equals(t3)){
            throw new MyException("ConditionalAssignment: variable and exp3 have different types");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return varName + "=(" + exp1 + ")?(" + exp2 + "):(" + exp3 + ")";
    }
}
