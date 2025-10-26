package model.value;
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
        return Type.BOOLEAN;
    }
    @Override
    public String toString(){
        return String.valueOf(value);
    }
}
