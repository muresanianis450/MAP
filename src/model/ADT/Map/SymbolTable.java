package model.ADT.Map;
import model.type.Type;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;


public class SymbolTable<K,V> implements IMap<K,V> {

    private final Map<String, Value> map = new HashMap<>();

    @Override
    public boolean isDefined(String variableName){ return map.containsKey(variableName); }

    @Override
    public Type getType(String variableName){
        return map.get(variableName).getType();
    }

    @Override
    /*in order to declare a new variable we must put a tuple of <variable_name, default value>
     we call the default value for out type, so it could be 0, NULL or however we define the
    default value to be.
    */
    public void declareVariable(String variableName, Value value){
        map.put(variableName,value);

    }

    @Override
    public void update(String variableName, Value value){
        map.put(variableName, value);
    }


    @Override
    public Value getValue(String variableName){
        return map.get(variableName);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public Map<String, Value> getContent() {
        return new HashMap<>(map); // return a copy for safety
    }

    @Override
    public IMap<String, Value> deepCopy() {
        IMap<String, Value> newMap = new SymbolTable();
        for (String key : map.keySet()) {
            newMap.declareVariable(key, map.get(key).deepCopy()); // deep copy of the Value
        }
        return newMap;
    }


}
