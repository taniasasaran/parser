public class Main {
    public static void main(String[] args) {
        String filename = "resources/g1.txt"; // Syntax.in from GitHub 1b
        Grammar grammar = new Grammar(filename);
        grammar.printProductions();
        System.out.println();
        grammar.printProductions("term");
        System.out.println();
        grammar.printNonTerminals();
        System.out.println();
        grammar.printTerminals();
        System.out.println();
        System.out.println(grammar.isCFG());
        //TODO: LR(0) Parser
    }
}