import java.util.ArrayList;

public class State {
    ArrayList<LRitem> items;

    public State(ArrayList<LRitem> items) {
        this.items = items;
    }

    public ArrayList<LRitem> getItems() {
        return items;
    }

    public void addItem(LRitem item) {
        items.add(item);
    }

    public void setItems(ArrayList<LRitem> items) {
        this.items = items;
    }

    public ArrayList<LRitem> closure(ArrayList<LRitem> I, Grammar enhancedGrammar) {
        ArrayList<LRitem> C = new ArrayList<>(I);
        ArrayList<LRitem> C1 = new ArrayList<>(C);
        boolean added = true;
        while (added) {
            added = false;
            C1 = new ArrayList<>(C);
            for (LRitem item : C) {
                // make a copy of C to avoid ConcurrentModificationException
                if (item.getBeta().isEmpty()) {
                    continue;
                }
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

    public String toString() {
        return items.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof State s)) {
            return false;
        }
        return this.items.equals(s.items);
    }
}
