import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Class Grammar (required operations: read a grammar from file, print set of nonterminals, set of terminals, set of productions, productions for a given nonterminal, CFG check)
public class Grammar {
    String filename;
    ArrayList<String> nonTerminals;
    ArrayList<String> terminals;
    Map<String, ArrayList<String>> productions;

    public Grammar(String filename) {
        this.filename = filename;
        this.nonTerminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new HashMap<>();
        readGrammar();
    }

    public void readGrammar() {
        return;
    }
}
