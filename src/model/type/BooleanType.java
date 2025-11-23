package model.type;
import model.value.BooleanValue;
import model.value.Value;
public class BooleanType implements Type{

@Override
    public Value defaultValue(){
    return new BooleanValue(false);
}
@Override
    public String toString(){
    return "bool";
}

@Override
    public boolean equals(Object another) {
    return another instanceof BooleanType;
}
}


