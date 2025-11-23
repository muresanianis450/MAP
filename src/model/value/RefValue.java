package model.value;

import model.type.RefType;
import model.type.Type;

public class RefValue implements Value {
    private final int address;
    private final Type locationType;

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return address;
    }
    public Type getLocationType() {
        return locationType;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType + ")";
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefValue otherRef)
            return address == otherRef.address &&
                    locationType.equals(otherRef.locationType);
        return false;
    }
}
