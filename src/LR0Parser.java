import java.util.*;

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

        for(int i = workingStack.size() - 1; i >= 0; i--){
            productionRHS.add(0,workingStack.get(i).first());

            if (Objects.equals(productionRHS, production.second())){
                State mMinusP = workingStack.get(i-1).second();
                State gotoResult = lrTable.getTable().get(mMinusP).targetStates().get(production.first());
                workingStack.subList(i, workingStack.size()).clear();
                workingStack.add(new Pair<>(production.first(), gotoResult));
                outputStack.add(0,lrTableEntry.reductionIndex());
                return;
            }
        }
    }

    public ArrayList<Integer> parse(List<String> input){
        workingStack = new ArrayList<>(Collections.singleton(new Pair<>("$", lrTable.getStartingState())));
        Collections.reverse(input);
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
            else {
                System.err.println("Parsing error!!!");
            }
        }
    }

    public ArrayList<ParsingTreeNode> transformStringInTable(ArrayList<Integer> string) {
        Grammar enhancedGrammar = lrTable.getEnhancedGrammar();
        ArrayList<ParsingTreeNode> table = new ArrayList<>();
        int productionNumber;
        ParsingTreeNode currentNode;
        ParsingTreeNode child;
        ParsingTreeNode sibling;
        int index = 0;
        ArrayList<ParsingTreeNode> stack = new ArrayList<>();
        ArrayList<String> Sproduction = enhancedGrammar.getProductionsNonterminal(enhancedGrammar.getStartingSymbol()).get(0);
        stack.add(new ParsingTreeNode(index, Sproduction.get(0)));
        index ++;
        int i = 0;
        while (i < string.size()) {
            while(!stack.isEmpty()){
                currentNode = stack.remove(stack.size() - 1);
                table.add(currentNode);
                // if currentNode is a terminal
                if (enhancedGrammar.getNonTerminals().contains(currentNode.getValue())) {
                    productionNumber = string.get(i);
                    sibling = null;
                    // add its children to the queue using productionNumber
                    Pair<String, ArrayList<String>> production = enhancedGrammar.getProduction(productionNumber);
                    if (production == null) {
                        continue;
                    }
                    ArrayList<String> rhs = production.second();
                    for (String symbol : rhs) {
                        child = new ParsingTreeNode(index, symbol);
                        child.setParent(currentNode);
                        child.setSibling(sibling);
                        stack.add(child);
                        index++;
                        sibling = child;
                    }
                    i++;
                }
            }
        }
        table.addAll(stack);
        table.sort(Comparator.comparing(ParsingTreeNode::getIndex));
        return table;
    }
}
