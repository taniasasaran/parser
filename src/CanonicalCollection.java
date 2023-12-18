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
        ArrayList<ArrayList<String>> Sproductions = enhancedGrammar.getProductionsNonterminal(Sprime);
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

    public ArrayList<ParsingTreeNode> transformStringInTable(String string) {
        ArrayList<ParsingTreeNode> table = new ArrayList<>();
        int productionNumber;
        ParsingTreeNode currentNode;
        ParsingTreeNode child;
        ParsingTreeNode sibling;
        int index = 0;
        ArrayList<ParsingTreeNode> queue = new ArrayList<>();
        queue.add(new ParsingTreeNode(index, enhancedGrammar.getStartingSymbol()));
        index ++;
        int i = 0;
        while (i < string.length()) {
            productionNumber = string.charAt(i) - '0';
            if(!queue.isEmpty()){
                currentNode = queue.remove(0);
                sibling = null;
                table.add(currentNode);
                // add its children to the queue using productionNumber
                Pair<String, ArrayList<String>> production = enhancedGrammar.getProduction(productionNumber);
                if (production == null) {
                    continue;
                }
                ArrayList<String> rhs = production.second();
                for (String symbol: rhs){
                    if (index == 5)
                        System.out.println("here");
                    child = new ParsingTreeNode(index, symbol);
                    child.setParent(currentNode);
                    child.setSibling(sibling);
                    queue.add(child);
                    index ++;
                    sibling = child;
                }
            }
            i++;
        }
        System.out.println(table);
        return table;
    }

}
