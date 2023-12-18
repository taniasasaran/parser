import java.util.ArrayList;
import java.util.Collections;

public class LR0Parser {
    private ArrayList<Pair<String, State>> workingStack;

    private ArrayList<String> inputStack = new ArrayList<>();

    private ArrayList<Integer> outputStack = new ArrayList<>();

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

    }

    public ArrayList<Integer> parse(ArrayList<String> input){
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
