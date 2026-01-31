package model.ADT.Map;

import exceptions.MyException;
import model.type.Type;
import model.value.Value;

import java.util.Map;

public interface IMap<K, V> {
    boolean isDefined(K key);

    V lookup(K key) throws MyException;     // or return null
    void add(K key, V value);
    void update(K key, V value);

    Map<K, V> getContent();

    IMap<K, V> deepCopy();


}

