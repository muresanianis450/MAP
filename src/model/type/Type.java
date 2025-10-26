package model.type;
import model.value.Value;
import model.value.IntegerValue;
import model.value.BooleanValue;

public enum Type {

    INTEGER,

    BOOLEAN;

    public Value getDefaultValue(){
        switch(this){
            case INTEGER:
                return new IntegerValue(0);
            case BOOLEAN:
                return new BooleanValue(false);
            default:
                throw new IllegalArgumentException("Unhandled type: "+ this);

        }
    }
}
