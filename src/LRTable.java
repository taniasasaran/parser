import java.util.*;

public class LRTable {
    private final Map<State, LRTableEntry> table = new HashMap<>();


    public LRTable(CanonicalCollection canonicalCollection) {
        generateTable(canonicalCollection);
    }

    public Map<State, LRTableEntry> getTable(){
        return table;
    }

    private void generateTable(CanonicalCollection canonicalCollection){
        System.out.println(canonicalCollection.getEnhancedGrammar().getStartingSymbol());
        int nrOfErrors = 0;
        for (State state : canonicalCollection.getStates()) {
            LRitem lRitem = state.getItems().get(0);
            Action action;


            if(!lRitem.beta.isEmpty()){
                action = Action.SHIFT;
            }
            //TODO: fix
            else if (Objects.equals(lRitem.alfa, new ArrayList<>(Collections.singleton(canonicalCollection.getEnhancedGrammar().getStartingSymbol())))
                    && state.getItems().size() == 1) {
                action = Action.ACCEPT;
            }
            else if(state.getItems().size() == 1) {
                action = Action.REDUCE;
            }
            else {
                action = Action.ERROR;
                nrOfErrors++;
            }

            LRTableEntry lrTableEntry = new LRTableEntry(canonicalCollection.getStateTransitions().get(state), action);

            table.put(state, lrTableEntry);
        }
        System.out.println("Number of conflicts:" + nrOfErrors);
    }
}
