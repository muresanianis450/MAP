package model.ADT.Map;

import model.type.Type;
import model.value.Value;

import java.util.Map;

public interface IMap<K,V> {

    boolean isDefined(String variableName);

    Type getType(String variableName);

    void declareVariable(String variableName, Value value);

    void update(String variableName, Value value);

    Value getValue(String variableName);

    Map<String, Value> getContent();

    IMap<String,Value> deepCopy();
}
