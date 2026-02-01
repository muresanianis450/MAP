package model.ADT.ProcTable;

import exceptions.MyException;
import model.statement.Statement;

import java.util.List;

public interface IProcTable {
    boolean isDefined(String name);
    void add(String name, List<String> params, Statement body) throws MyException;
    ProcData lookup(String name) throws MyException;
}
