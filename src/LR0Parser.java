import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LR0Parser {
    private ArrayList<Pair<String, State>> workingStack;

    private List<String> inputStack;

    private ArrayList<Integer> outputStack;

    private final LRTable lrTable;

    public LR0Parser(LRTable lrTable){
        this.lrTable = lrTable;
    }

    private void shift(){
        State state = workingStack.get(workingStack.size() - 1).second();
        LRTableEntry lrTableEntry = lrTable.getTable().get(state);
        String topOfInputStack = inputStack.get(inputStack.size() - 1);
        State gotoResult = lrTableEntry.targetStates().get(topOfInputStack);

        if(gotoResult == null){
            System.err.println("Parsing error!!!");
            return;
        }

        inputStack.remove(inputStack.size() - 1);

        workingStack.add(new Pair<>(topOfInputStack, gotoResult));
    }

    private void reduce(){
        State state = workingStack.get(workingStack.size() - 1).second();
        LRTableEntry lrTableEntry = lrTable.getTable().get(state);
        Pair<String, ArrayList<String>> production = lrTable.getEnhancedGrammar()
                .getProductionByIndex(lrTableEntry.reductionIndex());

        ArrayList<String> productionRHS = new ArrayList<>();

        //TODO: DOESN'T WORK!!!!!!
        for(int i = workingStack.size() - 1; i >= 0; i--){
            productionRHS.add(0,workingStack.get(i).first());

            if (Objects.equals(productionRHS, production.second())){
                State mMinusP = workingStack.get(i-1).second();
                State gotoResult = lrTable.getTable().get(mMinusP).targetStates().get(production.first());
                workingStack.subList(i -1, workingStack.size() - 1).clear();
                workingStack.add(new Pair<>(production.first(), gotoResult));
                return;
            }
        }
    }

    public ArrayList<Integer> parse(List<String> input){
        workingStack = new ArrayList<>(Collections.singleton(new Pair<>("$", lrTable.getStartingState())));
        inputStack = input;
        outputStack = new ArrayList<>();

        while (true){
            State state = workingStack.get(workingStack.size() - 1).second();
            if(lrTable.getTable().get(state).actionType() == Action.SHIFT){
                shift();
            }
            else if(lrTable.getTable().get(state).actionType() == Action.REDUCE){
                reduce();
            }
            else if(lrTable.getTable().get(state).actionType() == Action.ACCEPT){
                return outputStack;
            }
            else if(lrTable.getTable().get(state).actionType() == Action.SHIFT){
                System.err.println("Parsing error!!!");
            }
        }


    }
}
