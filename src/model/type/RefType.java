package model.type;

import model.value.Value;

public class RefType implements Type {
    private final Type inner;

    public RefType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefType otherRef)
            return inner.equals(otherRef.inner);
        return false;
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }

    @Override
    public Value defaultValue() {
        return new model.value.RefValue(0, inner);
    }
}
