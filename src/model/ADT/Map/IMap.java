package model.ADT.Map;

import model.type.Type;
import model.value.Value;

public interface IMap {

    boolean isDefined(String variableName);

    Type getType(String variableName);

    void declareVariable(String variableName, Type type);

    void update(String variableName, Value value);

    Value getValue(String variableName);

}
