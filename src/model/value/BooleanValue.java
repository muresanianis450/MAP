package model.value;
import model.type.BooleanType;
import model.type.Type;
public class BooleanValue implements Value {
    private final boolean value;

    public BooleanValue(boolean value){
        this.value = value;

    }
    public boolean getValue(){
        return value;
    }

    @Override
    public Type getType(){
        return new BooleanType();
    }
    @Override
    public String toString(){
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o){
        if ( this == o) return true;
        if(!(o instanceof BooleanValue other)) return false;
        return value == other.value;
    }
}
