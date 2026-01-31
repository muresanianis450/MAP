package model.ADT.LockTable;

import java.util.HashMap;
import java.util.Map;
public class LockTable implements ILockTable{
        private final Map<Integer,Integer> table = new HashMap<>();

        private int freeLocation = 0;

        @Override
        //synchronized so that no two threads can access it at the same time
    public synchronized int allocate() {
            freeLocation ++;
            table.put(freeLocation,-1); //-1 means "unlocked"
            return freeLocation;
        }

        @Override
    public synchronized boolean isDefined(int location) {
            return table.containsKey(location);
        }

    @Override
    public synchronized int get(int index) {
        return table.get(index);
    }

    @Override
    public synchronized void update(int location, int value) {
            table.put(location, value);}

    @Override
    public synchronized void setContent(Map<Integer, Integer > newContent) {
            table.clear();
            table.putAll(newContent);

            //keep freeLocation consistent (optional but nice :) )
        freeLocation = 0 ;
        for(Integer k: table.keySet()){
            if ( k > freeLocation) freeLocation = k;
        }


    }

    @Override
    public synchronized Map<Integer, Integer> getContent() {
        return new HashMap<>(table);
    }

    @Override
    public synchronized String toString(){
            return table.toString();
    }

}


