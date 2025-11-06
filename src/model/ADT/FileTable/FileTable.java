package model.ADT.FileTable;

import model.value.Value;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class FileTable implements IFileTable {
    private final Map<Value, BufferedReader> table;

    public FileTable() {
        this.table = new HashMap<>();
    }

    @Override
    public boolean isDefined(Value key) {
        return table.containsKey(key);
    }

    @Override
    public void add(Value key, BufferedReader reader) {
        table.put(key, reader);
    }

    @Override
    public BufferedReader lookup(Value key) {
        return table.get(key);
    }

    @Override
    public void remove(Value key) {
        BufferedReader br = table.remove(key);
        if (br != null) {
            try {
                br.close();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public Map<Value, BufferedReader> getContent() {
        return table;
    }

    @Override
    public void setContent(Map<Value, BufferedReader> content) {
        table.clear();
        table.putAll(content);
    }

    @Override
    public String toString() {
 //         return table.toString(); -> not working because it returns the toString to an object and won't know what to do

    StringBuilder sb = new StringBuilder();
    for(Value key : table.keySet()){
        sb.append(key.toString()).append("\n");
    }
    return sb.toString();
    }
}
