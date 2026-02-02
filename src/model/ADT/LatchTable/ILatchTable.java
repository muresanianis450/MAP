package model.ADT.LatchTable;

import exceptions.MyException;
import java.util.Map;

public interface ILatchTable {
    int allocate(int initialValue) ; // returns new index
    boolean isDefined(int index);
    int lookup(int index) throws MyException;
    void update(int index, int value );
    Map<Integer,Integer> getContent();
    void setContent(Map<Integer,Integer> content);
}
