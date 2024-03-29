import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String filename = "resources/g2short2.txt"; // Syntax.in from GitHub 1b
        String source = "resources/PIF2.out"; // pif from GitHub lab5
//        String filename = "resources/g1.txt"; // grammar from seminary
//        String source = "resources/seq.txt"; // sequence from seminary
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
//        for (var entry : lrTable.getTable().entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }


        LR0Parser lr0Parser = new LR0Parser(lrTable);
        // IF (2 > 1) { WRITE(2); } ELSE {WRITE(1);}
        ArrayList<Integer> parseResult = lr0Parser.parse(getTokens(source));

        if(parseResult == null){
            System.err.println("A parsing error occurred.");
            return;
        }
        for (Integer i : parseResult){
            System.out.println(i);
        }

        ArrayList<ParsingTreeNode> table = lr0Parser.transformStringInTable(parseResult);
        printTable(table);
        printTableToFile(table);
    }

    static ArrayList<String> getTokens(String source) {
        ArrayList<String> tokens = new ArrayList<>();
        // open file and read line by line
        try {
            Scanner scanner = new Scanner(new java.io.File(source));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split(" ");
                tokens.add(words[0]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    static void printTable(ArrayList<ParsingTreeNode> table){
        for (ParsingTreeNode parsingTreeNode : table) {
            System.out.println(parsingTreeNode);
        }
    }

    static void printTableToFile(ArrayList<ParsingTreeNode> table){
        try {
            java.io.File file = new java.io.File("output/table.out");
            java.io.PrintWriter output = new java.io.PrintWriter(file);
            for (ParsingTreeNode parsingTreeNode : table) {
                output.println(parsingTreeNode);
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}