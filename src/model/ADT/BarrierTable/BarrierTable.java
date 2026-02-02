package model.ADT.BarrierTable;

import exceptions.MyException;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BarrierTable implements IBarrierTable{
    private final Map<Integer,BarrierEntry> table = new HashMap<>();
    private int freeLocation = 0 ;

    private final Lock lock = new ReentrantLock(); // atomic operation

    private int getFreeLocation(){
        while(table.containsKey(freeLocation)) freeLocation++;
        return freeLocation;
    }
    @Override
    public int allocate(int required) {
        lock.lock();
        try{
            int loc = getFreeLocation();
            table.put(loc,new BarrierEntry(required, new ArrayList<>()));
            freeLocation++;
            return loc;
        }finally{
            lock.unlock();
        }
    }
    @Override
    public boolean isDefined(int index){
        lock.lock();
        try{
            return table.containsKey(index);
        }finally{
            lock.unlock();
        }
    }

    @Override
    public BarrierEntry lookup(int index) throws MyException{
        lock.lock();
        try{
            if(!table.containsKey(index)) throw new MyException("BarrierTable: index not defined: " + index);
            return table.get(index);
        }finally{
            lock.unlock();
        }
    }

    @Override
    public void update(int index, BarrierEntry entry ){
        lock.lock();
        try{
            table.put(index,entry);
        }finally{
            lock.unlock();
        }
    }

    @Override
    public Map<Integer,BarrierEntry> getContent(){
        lock.lock();
        try{
            return new HashMap<>(table);
        }finally{
            lock.unlock();
        }
    }
    @Override
    public void setContent(Map<Integer,BarrierEntry> newContent){
        lock.lock();
        try{
            table.clear();
            table.putAll(newContent);
            //keep freeLocation consistent
            freeLocation = 0;
            for(Integer k: table.keySet())
                if( k > freeLocation )
                    freeLocation = k;
            freeLocation++;
        }finally{
            lock.unlock();
        }
    }

    @Override
    public String toString(){
        return getContent().toString();
    }
}
