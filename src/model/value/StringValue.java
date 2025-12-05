package model.value;

import model.type.StringType;
import model.type.Type;

public class StringValue implements Value {
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) { //changed here!
        if (this == obj) return true;
        if (!(obj instanceof StringValue other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Value deepCopy(){
        return new StringValue(value);
    }
}
