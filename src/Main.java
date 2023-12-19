import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        String filename = "resources/g1.txt"; // Syntax.in from GitHub 1b
//        String filename = "resources/g1.txt"; // grammar from seminary
//        Grammar grammar = new Grammar(filename);
//        grammar.printProductions();
//        System.out.println();
//        grammar.printNonTerminals();
//        System.out.println();
//        grammar.printTerminals();
//        System.out.println();
//        System.out.println(grammar.isCFG());
        //TODO: LR(0) Parser
        Grammar enhancedGrammar = new Grammar(filename);
        CanonicalCollection canCol = new CanonicalCollection(enhancedGrammar);

        LRTable lrTable = new LRTable(canCol);
        for (var entry : lrTable.getTable().entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        LR0Parser lr0Parser = new LR0Parser(lrTable);

        for (Integer i : lr0Parser.parse(new ArrayList<>(Arrays.asList("c", "a")))){
            System.out.println(i);
        }

        canCol.transformStringInTable("124345");
    }
}