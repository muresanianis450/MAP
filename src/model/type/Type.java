package model.type;

import model.value.Value;
public interface Type {
    Value defaultValue();
    String toString();
}

// Type before changes to add subclasses for Type
/*package model.type;
import model.value.Value;
import model.value.IntegerValue;
import model.value.BooleanValue;
import model.value.StringValue;

import model.value.StringValue;
public enum Type {

    INTEGER{
        @Override
        public Value defaultValue(){
            return new IntegerValue(0);
        }
    },

    BOOLEAN{
        @Override
        public Value defaultValue(){
            return new BooleanValue(false);
        }
    },

    STRING{
        @Override
        public Value defaultValue(){
            return new StringValue("");
        }
    };

   /* public Value getDefaultValue(){
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
    }public abstract Value defaultValue();
}

_____________--_____________--_____________--_____________--_____________--
* */
