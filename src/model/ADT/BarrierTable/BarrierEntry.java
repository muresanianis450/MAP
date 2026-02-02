package model.ADT.BarrierTable;

import java.util.List;
public class BarrierEntry {

    private final int required; // N1
    private final List<Integer> waiting; // L1 (PrgState ids)


    public BarrierEntry(int required,List<Integer> waiting) {
        this.required = required;
        this.waiting = waiting;
    }

    public int getRequired(){return required;}
    public List<Integer> getWaiting(){return waiting;}

    @Override
    public String toString(){
        return "(" + required + "," + waiting + ")";
    }
}
