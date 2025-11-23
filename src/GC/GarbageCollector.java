package GC;

import model.value.RefValue;
import model.value.Value;

import java.util.*;
import java.util.stream.Collectors;

public class GarbageCollector {
    //get all addresses referenced directorly or indirectly

    public static Set<Integer> getReachableAddresses(Collection<Value> symTableValues, Map<Integer,Value> heap){
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> toVisit = new LinkedList<>();

        //add addresses from symbol table

        for( Value v :symTableValues){
            if(v instanceof RefValue ref){
                toVisit.add(ref.getAddress());
            }
        }
        //BFS/DFS to follow RefValues inside the heap
        while (!toVisit.isEmpty()) {

            int addr = toVisit.poll();
            if(!visited.contains(addr) && heap.containsKey(addr)){
                visited.add(addr);
                Value heapVal = heap.get(addr);
                if(heapVal instanceof RefValue refInHeap){
                    toVisit.add(refInHeap.getAddress());
                }
            }
        }
        return visited;
    }

    //filter the heap to only keep reachable addresses
    public static Map<Integer, Value> safeGarbageCollector(Collection<Value> symTableValues, Map<Integer,Value> heap){
        Set<Integer> reachable = getReachableAddresses(symTableValues, heap);
        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

}
