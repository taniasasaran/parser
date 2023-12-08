import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class State {
    ArrayList<LRitem> items;

    public State(ArrayList<LRitem> items) {
        this.items = items;
    }

    public ArrayList<LRitem> closure(ArrayList<LRitem> I, Grammar enhancedGrammar) {
        ArrayList<LRitem> C = new ArrayList<>(I);
        // algorithm closure
        return C;
    }
}
