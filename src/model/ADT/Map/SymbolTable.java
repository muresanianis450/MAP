package model.ADT.Map;
import exceptions.MyException;
import model.type.Type;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;


public class SymbolTable<K, V> implements IMap<K, V> {
    private final Map<K, V> map = new HashMap<>();

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public V lookup(K key) throws MyException {
        if (!map.containsKey(key))
            throw new MyException("Key not defined: " + key);
        return map.get(key);
    }

    @Override
    public void add(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void update(K key, V value) {
        map.put(key, value);
    }

    @Override
    public Map<K, V> getContent() {
        return new HashMap<>(map);
    }

    @Override
    public IMap<K, V> deepCopy() {
        SymbolTable<K, V> copy = new SymbolTable<>();
        copy.map.putAll(this.map);
        return copy;
    }

    @Override
    public String toString() {
        return map.toString();
    }

}

