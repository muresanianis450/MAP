package model.ADT.BarrierTable;

import exceptions.MyException;
import java.util.Map;
public interface IBarrierTable {

    int allocate(int required); //returns new index
    boolean isDefined(int index);
    BarrierEntry lookup(int index) throws MyException;
    void update(int index, BarrierEntry entry); //atomic
    Map<Integer,BarrierEntry> getContent();
    void setContent(Map<Integer,BarrierEntry> newContent);

}

