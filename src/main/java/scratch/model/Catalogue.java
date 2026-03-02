package scratch.model;

import java.util.Set;

public class Catalogue {
    private Set<Action> catalogue;

    public boolean addToCatalogue(Action action){
        return catalogue.add(action);
    }

    public boolean addToProgram(){
        return true;
    }
}
