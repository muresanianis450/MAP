package model.ADT.SemaphoreTable;

import java.util.Map;
import java.util.List;
import java.util.concurrent.Semaphore;

public interface ISemaphoreTable {
    //create a new entry ( number1, empty list) and return its new free location

    int allocate(int number1);

    boolean isDefined(int index);

    SemaphoreEntry get(int location);

    void update(int location, SemaphoreEntry entry);

    Map<Integer,SemaphoreEntry> getContent();

    void setContent(Map<Integer,SemaphoreEntry> newContent);
}
