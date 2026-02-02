package model.ADT.SemaphoreTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SemaphoreTable implements ISemaphoreTable {

    private final Map<Integer, SemaphoreEntry> table = new HashMap<>();
    private int freeLocation = 0;


    @Override
    public synchronized int allocate(int number1){
        freeLocation++;
        table.put(freeLocation, new SemaphoreEntry(number1, new ArrayList<>()));
        return freeLocation;
    }

    @Override
    public synchronized boolean isDefined(int location) {
        return table.containsKey(location);
    }

    @Override
    public synchronized SemaphoreEntry get(int location) {
        return table.get(location);
    }

    @Override
    public synchronized void update(int location, SemaphoreEntry entry) {
        table.put(location, entry);
    }

    @Override
    public synchronized Map<Integer,SemaphoreEntry> getContent(){
        return new HashMap<>(table);
    }
    @Override
    public synchronized void setContent(Map<Integer,SemaphoreEntry> newContent){
        table.clear();
        table.putAll(newContent);

        //keep freeLocation consistent (optional but nice :) )
        freeLocation = 0 ;
        for(Integer k: table.keySet()){
            if ( k > freeLocation) freeLocation = k;
        }
    }

    @Override
    public synchronized String toString(){
        return table.toString();
    }
}
