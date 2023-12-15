import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Grammar {
    String filename;
    String startingSymbol;
    ArrayList<String> nonTerminals;
    ArrayList<String> terminals;
    Map<String, ArrayList<String>> productions;  // key: nonterminal (lhs), value: list of expressions (rhs)
    Boolean isCFG;

    public Grammar(String filename) {
        this.filename = filename;
        this.nonTerminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new HashMap<>();
        isCFG = true;
        readGrammar();
    }

    public ArrayList<String> getNonTerminals() {
        return nonTerminals;
    }

    public ArrayList<String> getTerminals() {
        return terminals;
    }

    public ArrayList<String> getGrammarSymbols(){
        ArrayList<String> grammarSymbols = new ArrayList<>();
        grammarSymbols.addAll(nonTerminals);
        grammarSymbols.addAll(terminals);
        return grammarSymbols;
    }

    public Map<String, ArrayList<String>> getProductions() {
        return productions;
    }

    public ArrayList<ArrayList<String>> getProductionsNonterminal(String nonTerminal){
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for (ArrayList<String> production : productions.values()) {
            result.add(production);
        }
        return result;
    }

    private void setFalseCFG() {
        isCFG = false;
    }

    public void readGrammar() {
        // read from file and populate nonTerminals, terminals, productions
        // read from file filename row by row
        // for each row, split it into lhs and rhs
        // add lhs to nonTerminals cleaning it up (remove <> for bnf)
        // add lhs, rhs to productions (lhs is key, rhs is value)
        // for each symbol in rhs, if it has <> around it, add it to nonTerminals(bnf)
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // split line into lhs and rhs once we find the "::=" sign (lhs ::= rhs) rhs can contain ::= sign
                String[] splitLine = line.split("::=", 2);
                String lhs = splitLine[0].trim();
                String rhs = splitLine[1].trim();
                String[] rhsSymbols = rhs.split("\\|");
                // split even further by spaces
                ArrayList<ArrayList<String>> rhsSymbolsList = new ArrayList<>();
                for (String rhsSymbol : rhsSymbols) {
                    String[] rhsSymbolSplit = rhsSymbol.trim().split(" ");
                    ArrayList<String> rhsSymbolList = new ArrayList<>(Arrays.asList(rhsSymbolSplit));
                    // eliminate <> from symbols in rhsSymbolList with regex and streams
                    rhsSymbolList.replaceAll(s -> s.replaceAll("^<|>$", ""));
                    rhsSymbolsList.add(rhsSymbolList);
                }

                for (ArrayList<String> rhsList: rhsSymbolsList) {
                    for (String rhsSymbol : rhsList) {
                        // remove leading and trailing whitespace and <> for bnf
                        String rhsSymbolClean = rhsSymbol.trim();
                        if (rhsSymbolClean.startsWith("\"") && rhsSymbolClean.endsWith("\"")) {
                            rhsSymbolClean = rhsSymbolClean.replaceAll("^\"|\"$", "");
                            if (!terminals.contains(rhsSymbolClean)) {
                                terminals.add(rhsSymbolClean);
                            }
                        } else if (!nonTerminals.contains(rhsSymbolClean)){
                            nonTerminals.add(rhsSymbolClean);
                        }
                    }
                }

                // eliminate "" from terminals from rhsSymbolsList with regex and streams
                rhsSymbolsList.forEach(rhsList -> rhsList.replaceAll(s -> s.replaceAll("^\"|\"$", "")));

                // add lhs, rhs to productions (lhs is key, rhs is value)
                String [] lhsSplit = lhs.split(" ");
                if (lhsSplit.length > 1) {
                    setFalseCFG(); // if lhs has more than one symbol, it's not a CFG
                } else {
                    lhs = lhs.replaceAll("^<|>$", "");
                    //the lhs of the first production is the
                    if(productions.isEmpty()){
                        startingSymbol = lhs;
                    }
                    for (ArrayList<String> rhsList: rhsSymbolsList) {
                        productions.put(lhs, rhsList);
                    }
                    if (!nonTerminals.contains(lhs)) {
                        nonTerminals.add(lhs);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printNonTerminals() {
        for (String nonTerminal : nonTerminals) {
            System.out.println(nonTerminal);
        }
    }

    public void printTerminals() {
        for (String terminal : terminals) {
            System.out.println(terminal);
        }
    }

    public void printProductions() {
        productions.forEach((nonTerminal, symbols) -> {
            System.out.print(nonTerminal + " -> ");
            for (String symbol : symbols) {
                System.out.print(symbol + " ");
            }
            System.out.println();
        });
    }

    public void printProductions(String nonTerminal) {
        ArrayList<String> symbols = this.productions.get(nonTerminal);
        System.out.print(nonTerminal + " -> ");
        for (String symbol : symbols) {
            System.out.print(symbol + " ");
        }
        System.out.println();
    }

    public boolean isCFG() {
        return isCFG;
    }
}
