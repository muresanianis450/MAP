package model.type;
import model.value.Value;
import model.value.IntegerValue;
import model.value.BooleanValue;
import model.value.StringValue;

import model.value.StringValue;
public enum Type {

    INTEGER,

    BOOLEAN,

    STRING;

    public Value getDefaultValue(){
        switch(this){
            case INTEGER:
                return new IntegerValue(0);
            case BOOLEAN:
                return new BooleanValue(false);
            case STRING:
                return new StringValue("");
            default:
                throw new IllegalArgumentException("Unhandled type: "+ this);

        }
    }
}
