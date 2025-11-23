package model.ADT.Heap;

import model.value.Value;
import java.util.Map;
public interface IHeap {
    int allocate(Value value); // returns new address

    void update(int address, Value value);
    Value get(int address);
    boolean isDefined(int address);
    Map<Integer,Value> getContent();
    void setContent(Map<Integer,Value> newContent);

}
