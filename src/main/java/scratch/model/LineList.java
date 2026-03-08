package scratch.model;

import java.util.ArrayList;
import java.util.List;

public class LineList {
    private final List<MyLine> lines = new ArrayList<>();

    public  boolean add(MyLine line) {
        return lines.add(line);
    }

    public List<MyLine> getLines() {
        return lines;
    }

    public void clear() {
        lines.clear();
    }
}
