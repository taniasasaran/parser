import java.util.*;

public class CanonicalCollection {
    private final Grammar enhancedGrammar;
    private final HashMap<State, Map<String, State>> stateTransitions;

    private final ArrayList<State> states;

    public CanonicalCollection(Grammar enhancedGrammar) {
        this.enhancedGrammar = enhancedGrammar;
        this.stateTransitions = new HashMap<>();
        this.states = generate();
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

    public ArrayList<State> generate(){
        ArrayList<State> states = new ArrayList<>();
        ArrayList<State> statesCopy;
        // s0 = closure({[S'->.S]})
        State s0 = new State(new ArrayList<>());
        String Sprime = enhancedGrammar.startingSymbol;
        ArrayList<String> Sproductions = enhancedGrammar.productions.get(Sprime);
        s0.addItem(new LRitem(Sprime, new ArrayList<>(), Sproductions));
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
                    if (!gotoResult.isEmpty()) {
                        if (!statesCopy.contains(new State(gotoResult))) {
                            // add goto(s, X) to states
                            State newState = new State(gotoResult);
                            statesCopy.add(newState);
                            changed = true;
                        }
                        if(stateTransitions.containsKey(s)){
                            stateTransitions.get(s).put(X, getState(gotoResult, statesCopy));
                        }
                        else{
                            Map<String, State> result = new HashMap<>();
                            result.put(X, getState(gotoResult, statesCopy));
                            stateTransitions.put(s, result);
                        }
                    }
                }
            }
            states = new ArrayList<>(statesCopy);
        }
        return states;
    }

    private State getState(ArrayList<LRitem> gotoResult, ArrayList<State> states) {
        for (State state : states) {
            if (state.getItems().equals(gotoResult)) {
                return state;
            }
        }
        return null;
    }

    public Grammar getEnhancedGrammar() {
        return enhancedGrammar;
    }

    public List<State> getStates(){
        return states;
    }

    public HashMap<State, Map<String, State>> getStateTransitions(){
        return stateTransitions;
    }

}
