package model.ADT.ProcTable;


import exceptions.MyException;
import model.statement.Statement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcTable implements IProcTable{

private final Map<String,ProcData> table = new HashMap<>();

    @Override
    public boolean isDefined(String name) {return table.containsKey(name);}

    @Override
    public void add(String name, List<String> params, Statement body) throws MyException{
        if(table.containsKey(name)){
            throw  new MyException("Procedure already defined: " + name);
        }
        table.put(name, new ProcData(params,body));
    }

    @Override
    public String toString(){return table.toString();}

    @Override
    public ProcData lookup(String name) throws MyException{
        ProcData data = table.get(name);
        if(data == null) throw new MyException("Undefined procedure: " + name);
        return data;
    }
}
