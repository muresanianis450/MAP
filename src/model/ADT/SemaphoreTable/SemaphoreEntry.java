package model.ADT.SemaphoreTable;

import java.util.ArrayList;
import java.util.List;

public class SemaphoreEntry {

    private final int permits; //N1
    private final List<Integer> acquiredBy ; // List1 (ProgramState ids)

    public SemaphoreEntry(int permits,List<Integer> acquiredBy){

        this.permits = permits;
     //defensive copy so outside code can't mutate internal list silently
        this.acquiredBy = new ArrayList<>(acquiredBy);
    }

    public int getPermits(){
        return permits;
    }

    //return a copy for safety
    public List<Integer> getAcquiredBy(){
        return new ArrayList<>(acquiredBy);
    }
    //internal helper: build a new entry with same permits but a different list
    public SemaphoreEntry withAcquiredBy(List<Integer> newAcquiredBy){
        return new SemaphoreEntry(this.permits, newAcquiredBy);
    }

    @Override
    public String toString(){
        return "(" + permits + ", " + acquiredBy.toString() + ")";
    }
}
