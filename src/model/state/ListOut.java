package model.state;
import java.util.ArrayList;
import java.util.List;

public class ListOut implements Out{

    private final List<Object> outputList;

    public ListOut(){
        outputList = new ArrayList<>();
    }

    @Override
    public void add(Object value){
        outputList.add(value);

    }
    @Override
    public String toString(){
        return outputList.toString();
    }
}
