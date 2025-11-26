package model.ADT.Heap;

import model.value.Value;
import java.util.Map;

import java.util.HashMap;
public class Heap implements IHeap {
    private final Map<Integer,Value> heap;
    private int freeAddress; // next free address

    public Heap(){
        this.heap = new HashMap<>();
        this.freeAddress = 0;
    }

    private int getFreeAddress(){
        while(heap.containsKey(freeAddress)) // find next free address (neccessary if we allow deallocation/ garbage collection)
            freeAddress++;

        return freeAddress;
    }

    @Override
    public int allocate(Value value){
        int address = getFreeAddress();
        heap.put(address, value);
        freeAddress++;
        return address;
    }


    @Override
    public void update(int address, Value value){
        heap.put(address, value);
    }
    @Override
    public Value get(int address){
        return heap.get(address);
    }
    @Override
    public boolean isDefined(int address){
        return heap.containsKey(address);
    }
    @Override
    public Map<Integer,Value> getContent(){
        return heap;
    }
    @Override
    public void setContent(Map<Integer,Value> newContent){
        heap.clear();
        heap.putAll(newContent);
    }
    @Override
    public String toString(){
        return heap.toString();
    }
}
