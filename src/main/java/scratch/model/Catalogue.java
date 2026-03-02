package scratch.model;

import java.util.HashSet;
import java.util.Set;

public class Catalogue {
    private final Set<Action> catalogue = new HashSet<>();

    public boolean add(Action action){
        return catalogue.add(action);
    }

    public boolean addToProgram(){
        return true;
    }
}
