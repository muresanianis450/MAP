package model.value;
import model.type.Type;
public class IntegerValue implements Value{
    private final int value;

    public IntegerValue(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }


    @Override
    public Type getType() {
        return Type.INTEGER;}

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
