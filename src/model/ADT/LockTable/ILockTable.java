package model.ADT.LockTable;

import java.util.Map;
public interface ILockTable {
    int allocate(); // returns new lock index, initialized with -1
    boolean isDefined(int index) ;
    int get(int index);
    void update(int index, int value);

    Map<Integer, Integer> getContent();

    void setContent(Map<Integer,Integer> newContent);
}
