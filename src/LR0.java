import java.util.*;

public class LR0 {
    private Grammar enhancedGrammar;

    public LR0(Grammar enhancedGrammar) {
        this.enhancedGrammar = enhancedGrammar;
    }
    public ArrayList<LRitem> goTo(State s, String symbol) {
        ArrayList<LRitem> result = new ArrayList<>();
        for (LRitem item : s.getItems()) {
            if (item.getBeta().isEmpty()) {
                continue;
            }
            if (item.getBeta().get(0).equals(symbol)) {
                result.add(item.constructNewDotShift());
            }
        }
        return s.closure(result, enhancedGrammar);
    }

    public ArrayList<State> canonicalCollection(Grammar enhancedGrammar){
        ArrayList<State> states = new ArrayList<>();
        ArrayList<State> statesCopy;
        // s0 = closure({[S'->.S]})
        State s0 = new State(new ArrayList<>());
        String Sprime = enhancedGrammar.startingSymbol;
        ArrayList<ArrayList<String>> Sproductions = enhancedGrammar.productions.get(Sprime);
        for (ArrayList<String> production : Sproductions) {
            s0.addItem(new LRitem(Sprime, new ArrayList<>(), production));
        }
        s0.setItems(s0.closure(s0.getItems(), enhancedGrammar));
        states.add(s0);
        // for each state s in states
        boolean changed = true;
        while (changed) {
            changed = false;
            statesCopy = new ArrayList<>(states);
            for (State s: states) {
                // for each grammar symbol X
                for (String X : enhancedGrammar.getGrammarSymbols()) {
                    // if goto(s, X) is not empty and not in states
                    ArrayList<LRitem> gotoResult = goTo(s, X);
                    if (!gotoResult.isEmpty() && !statesCopy.contains(new State(gotoResult))) {
                        // add goto(s, X) to states
                        State newState = new State(gotoResult);
                        statesCopy.add(newState);
                        changed = true;
                    }
                }
            }
            states = new ArrayList<>(statesCopy);
        }
        return states;
    }

    public void printStates(ArrayList<State> states){
        for (State state : states) {
            System.out.println(state);
        }
    }
}
