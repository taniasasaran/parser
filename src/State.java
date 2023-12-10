import java.util.ArrayList;

public class State {
    ArrayList<LRitem> items;

    public State(ArrayList<LRitem> items) {
        this.items = items;
    }

    public ArrayList<LRitem> getItems() {
        return items;
    }

    public ArrayList<LRitem> closure(ArrayList<LRitem> I, Grammar enhancedGrammar) {
        ArrayList<LRitem> C = new ArrayList<>(I);
        ArrayList<LRitem> C1 = new ArrayList<>(C);
        boolean added = true;
        while (added) {
            added = false;
            for (LRitem item : C) {
                // make a copy of C to avoid ConcurrentModificationException
                C1 = new ArrayList<>(C);
                String B = item.getBeta().get(0);
                if (enhancedGrammar.getNonTerminals().contains(B)) {
                    // for B->y in P do
                    for (ArrayList<String> rhs : enhancedGrammar.getProductions().get(B)) {
                        // if B->.y not in C then
                        LRitem newItem = new LRitem(B, new ArrayList<>(), rhs);
                        if (!C1.contains(newItem)) {
                            C1.add(newItem);
                            added = true;
                        }
                    }
                }
            }
            // add all items from C1 to C
            C = new ArrayList<>(C1);
        }
        return C;
    }
}
