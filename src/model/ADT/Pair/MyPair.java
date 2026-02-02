package model.ADT.Pair;

public class MyPair <A,B>{
    private A first;
    private B second;
    public MyPair(A first, B second){
        this.first = first;
        this.second = second;
    }

    public A getFirst(){
        return first;
    }
    public B getSecond(){
        return second;
    }
    public void setFirst(A first){
        this.first = first;
    }
    public void setSecond(B second){
        this.second = second;}

    @Override
    public String toString(){
        return "(" + first.toString() + "," + second.toString() + ")";
    }
}
