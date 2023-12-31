import java.util.*;

public class LRTable {
    private final Map<State, LRTableEntry> table = new HashMap<>();

    private final State startingState;

    private final Grammar enhancedGrammar;

    public Grammar getEnhancedGrammar() {
        return enhancedGrammar;
    }

    public LRTable(CanonicalCollection canonicalCollection) {
        generateTable(canonicalCollection);
        enhancedGrammar = canonicalCollection.getEnhancedGrammar();
        startingState = canonicalCollection.getStartingState();
    }

    public Map<State, LRTableEntry> getTable(){
        return table;
    }

    public State getStartingState() {
        return startingState;
    }

    private void generateTable(CanonicalCollection canonicalCollection){
        System.out.println(canonicalCollection.getEnhancedGrammar().getStartingSymbol());
        int nrOfErrors = 0;
        for (State state : canonicalCollection.getStates()) {
            LRitem lRitem = state.getItems().get(0);
            Action action;
            Integer reductionIndex = null;

            if(!lRitem.beta.isEmpty()){
                action = Action.SHIFT;
            }
            else if (Objects.equals(lRitem.lhs, canonicalCollection.getEnhancedGrammar().getStartingSymbol())
                    && lRitem.beta.isEmpty()
                    && state.getItems().size() == 1) {
                action = Action.ACCEPT;
            }
            else if(state.getItems().size() == 1) {
                action = Action.REDUCE;
                reductionIndex = canonicalCollection.getEnhancedGrammar().getProductionIndex(lRitem.lhs, lRitem.alfa);
            }
            else {
                action = Action.ERROR;
                nrOfErrors++;
            }

            LRTableEntry lrTableEntry = new LRTableEntry(canonicalCollection.getStateTransitions().get(state),
                    action,
                    reductionIndex);

            table.put(state, lrTableEntry);
        }
        System.out.println("Number of conflicts:" + nrOfErrors);
    }
}
