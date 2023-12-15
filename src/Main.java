import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
//        String filename = "resources/g2.txt"; // Syntax.in from GitHub 1b
        String filename = "resources/g1.txt"; // grammar from seminary
        Grammar grammar = new Grammar(filename);
        grammar.printProductions();
        System.out.println();
        grammar.printNonTerminals();
        System.out.println();
        grammar.printTerminals();
        System.out.println();
        System.out.println(grammar.isCFG());
        //TODO: LR(0) Parser
//        Grammar enhancedGrammar = new Grammar(filename);
//        CanonicalCollection canCol = new CanonicalCollection(enhancedGrammar);
//        ArrayList<State> states = canCol.generate(enhancedGrammar);
//        canCol.printStates(states);
    }
}