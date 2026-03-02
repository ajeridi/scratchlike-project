package scratch.model;

import java.util.ArrayList;
import java.util.List;

public class ActionList {
    private final List<Action> actions = new ArrayList<>();

    public boolean add(Action action) {
        return actions.add(action);
    }
}
