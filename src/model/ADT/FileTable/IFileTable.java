package model.ADT.FileTable;

import model.value.Value;

import java.io.BufferedReader;
import java.util.Map;

public interface IFileTable {
    boolean isDefined(Value key);
    void add(Value key, BufferedReader reader);
    BufferedReader lookup(Value key);
    void remove(Value key);
    Map<Value, BufferedReader> getContent();
    void setContent(Map<Value, BufferedReader> content);
}