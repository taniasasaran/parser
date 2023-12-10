import java.util.*;

public class LR0 {
    private Grammar enhancedGrammar;

    public LR0(Grammar enhancedGrammar) {
        this.enhancedGrammar = enhancedGrammar;
    }
    public ArrayList<LRitem> goTo(State s, String symbol) {
        ArrayList<LRitem> result = new ArrayList<>();
        for (LRitem item : s.getItems()) {
            if (item.getBeta().get(0).equals(symbol)) {
                result.add(item.constructNewDotShift());
            }
        }
        return s.closure(result, enhancedGrammar);
    }

    public ArrayList<State> canonicalCollection(Grammar enhancedGrammar){
        return new ArrayList<>();
    }
}
