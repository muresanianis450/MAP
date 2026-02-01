package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.ADT.Map.SymbolTable;
import model.ADT.ProcTable.ProcData;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
import model.value.Value;

import java.util.ArrayList;
import java.util.List;
//Call pushes a new local environment
// call does a procedure invocation
// reuses code: define sum(a,b), call it 100 times


/*
/ looks up fname in the Proctable
Evaluates arguments (exp1... expn ) using the current symtable + heap
creates a new SymTable {  a -> val1 , b -> val2}
pushes that new SymTable on the SymTableStack
pushes the procedure body to execute ( plus a return after it )

 */
public class CallStatement implements Statement{
    private final String fname;
    private final List<Expression> args;



    public CallStatement(String fname, List<Expression> args){
        this.fname = fname;
        this.args = args;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException
    {
        //lookup proc
        ProcData proc = state.procTable().lookup(fname);

        List<String> formals = proc.params();
        Statement body = proc.body();

        if(formals.size() != args.size())
            throw new MyException("Call " + fname + ": expected "+ formals.size()+ " args, got " + args.size());


        //evaluate actual arguments in current symTable

        List<Value> actualVals = new ArrayList<>();

        for(Expression e : args){
            actualVals.add(e.evaluate(state.symbolTable(),state.heap()));
        }

        //create new local symTable mapping formal -> actual
        IMap<String,Value> local = new SymbolTable<>();
        for(int i = 0 ; i < formals.size() ; i++ ){
            local.add(formals.get(i) , actualVals.get(i));
        }

        //push local scope
        state.pushSymTable(local);

        //push return then body
        state.executionStack().push(new ReturnStatement());
        state.executionStack().push(body.deepCopy());

        return null;
    }


    @Override
    public Statement deepCopy() {
        return new CallStatement(fname,args.stream().map(Expression::deepCopy).toList());
    }


    @Override
    public IMap<String,Type> typeCheck(IMap<String, Type> typeEnv) throws MyException{
        for(Expression e : args) e.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString(){
        return "call " + fname+ args;
    }


}
