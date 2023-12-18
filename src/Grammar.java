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
    Map<String, ArrayList<ArrayList<String>>> productions;  // key: nonterminal (lhs), value: list of expressions (rhs)
    Map<Pair<String, ArrayList<String>>, Integer> productionsIndexed; // key: index, value: pair of nonterminal and rhs
    Boolean isCFG;
    Integer index;

    public Grammar(String filename) {
        this.filename = filename;
        this.nonTerminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new HashMap<>();
        this.productionsIndexed = new HashMap<>();
        index = 1;
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

    public Map<String, ArrayList<ArrayList<String>>> getProductions() {
        return productions;
    }

    public ArrayList<ArrayList<String>> getProductionsNonterminal(String nonTerminal){
        return productions.get(nonTerminal);
    }

    public String getStartingSymbol() {
        return startingSymbol;
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
                    if (!productions.containsKey(lhs)) {
                        productions.put(lhs, rhsSymbolsList);
                    } else {
                        productions.get(lhs).addAll(rhsSymbolsList);
                    }
                    for (ArrayList<String> rhsList: rhsSymbolsList) {
                        productionsIndexed.put(new Pair<>(lhs, rhsList), index);
                        index++;
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

    public Integer getProductionIndex(String nonTerminal, ArrayList<String> rhs){
        return productionsIndexed.get(new Pair<>(nonTerminal, rhs));
    }

    public void printProductionsIndexed() {
        for (Map.Entry<Pair<String, ArrayList<String>>, Integer> entry : productionsIndexed.entrySet()) {
            System.out.println("(" + entry.getValue() + ")" + entry.getKey());
        }
    }

    public Pair<String, ArrayList<String>> getProduction(Integer index){
        if (index == 0 || index > this.index) {
            return null;
        }
        for (Map.Entry<Pair<String, ArrayList<String>>, Integer> entry : productionsIndexed.entrySet()) {
            if (entry.getValue().equals(index)) {
                return entry.getKey();
            }
        }
        return null;
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
            for (ArrayList<String> symbol : symbols) {
                for (String s : symbol) {
                    System.out.print(s + " ");
                }
                if (symbols.indexOf(symbol) != symbols.size() - 1)
                    System.out.print("| ");
            }
            System.out.println();
        });
    }

    public void printProductions(String nonTerminal) {
        ArrayList<ArrayList<String>> symbols = this.productions.get(nonTerminal);
        System.out.print(nonTerminal + " -> ");
        for (ArrayList<String> symbol : symbols) {
            for (String s : symbol) {
                System.out.print(s + " ");
            }
            if (symbols.indexOf(symbol) != symbols.size() - 1)
                System.out.print("| ");
        }
        System.out.println();
    }

    public boolean isCFG() {
        return isCFG;
    }
}