package model.ADT.LatchTable;


import exceptions.MyException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LatchTable  implements ILatchTable{

    private final Map<Integer,Integer> table = new HashMap<>();
    private int freeLocation = 0 ;
    private final Lock lock = new ReentrantLock(); // we use reentrant lock because it can be acquired multiple times by the same thread

    private int getFreeLocation(){
        while(table.containsKey(freeLocation))
            freeLocation++;
        return freeLocation;
    }

    @Override
    public int allocate(int initialValue){
        lock.lock();
        try{
            int loc = getFreeLocation();
            table.put(loc,initialValue);
            freeLocation++;
            return loc;
        }finally {
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
    public int lookup(int index) throws MyException{
        lock.lock();
        try{
            if (!table.containsKey(index)) {
                throw new MyException("LatchTable: Index not defined: " + index);
            }
        }finally {
            lock.unlock();
        }
        return table.get(index);
    } //TODO VERIFY RETURN STATEMENT

    @Override
    public void update(int index, int value) {
        lock.lock();
        try{
            table.put(index,value);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Integer,Integer> getContent(){
        lock.lock();
        try{
            return new HashMap<>(table);
        }finally{
            lock.unlock();
        }
    }

    @Override
    public void setContent(Map<Integer,Integer> newContent){
        lock.lock();
        try{
            table.clear();
            table.putAll(newContent);
            freeLocation = 0;
            for(Integer k : table.keySet()){
                if ( k > freeLocation ) freeLocation = k;
            }
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
