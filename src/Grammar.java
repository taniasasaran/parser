import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Grammar {
    String filename;
    ArrayList<String> nonTerminals;
    ArrayList<String> terminals;
    //TODO: change this map to String, ArrayList<ArrayList<String>>
    // it should support A -> B C | D E | F G as A: [[B, C], [D, E], [F, G]]
    Map<String, ArrayList<ArrayList<String>>> productions;  // key: nonterminal (lhs), value: list of expressions (rhs)
    Map<ArrayList<String>, ArrayList<ArrayList<String>>> productionsCG; // only contextful productions

    public Grammar(String filename) {
        this.filename = filename;
        this.nonTerminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new HashMap<>();
        this.productionsCG = new HashMap<>();
        readGrammar();
    }

    public void readGrammar() {
        // TODO: need to change grammar in bnf and redo part of this function
        // read from file and populate nonTerminals, terminals, productions, productionsCG
        // read from file filename row by row
        // for each row, split it into lhs and rhs
        // add lhs to nonTerminals
        // add lhs, rhs to productions (lhs is key, rhs is value)
        // add lhs, rhs to productionsCG (lhs is key, rhs is value) only if lhs is a list
        // for each symbol in rhs, if it has <> around it, add it to nonTerminals(bnf) or "" to terminals(ebnf)
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // split line into lhs and rhs once we find the "=" sign (lhs = rhs) rhs can contain = sign
                // TODO: needs change if bnf is used
                String[] splitLine = line.split("=", 2);
                String lhs = splitLine[0].trim();
                String rhs = splitLine[1].trim();
                String[] rhsSymbols = rhs.split("\\|");
                // split even further by spaces
                ArrayList<ArrayList<String>> rhsSymbolsList = new ArrayList<>();
                for (String rhsSymbol : rhsSymbols) {
                    String[] rhsSymbolSplit = rhsSymbol.trim().split(" ");
                    ArrayList<String> rhsSymbolList = new ArrayList<>(Arrays.asList(rhsSymbolSplit));
                    rhsSymbolsList.add(rhsSymbolList);
                }

                // add lhs, rhs to productions (lhs is key, rhs is value)
                // add lhs, rhs to productionsCG only if lhs is a list (lhs is key, rhs is value)
                //TODO: todoododo
                String [] lhsSplit = lhs.split(" ");
                if (lhsSplit.length > 1) {
                    ArrayList<String> lhsList = new ArrayList<>(Arrays.asList(lhsSplit));
//                    productionsCG.put(lhsList, new ArrayList<>()(Arrays.asList(rhsSymbols)));
                } else {
//                    productions.put(lhs, new ArrayList<>(Arrays.asList(rhsSymbols)));

                    if (!nonTerminals.contains(lhs)) {
                        nonTerminals.add(lhs);
                    }
                }
                // for each symbol in rhs, add it to terminals or nonTerminals
                for (ArrayList<String> rhsList: rhsSymbolsList) {
                    for (String rhsSymbol : rhsList) {
                        // remove leading and trailing whitespace and "" for ebnf / <> for bnf
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
        for (Map.Entry<String, ArrayList<ArrayList<String>>> entry : productions.entrySet()) {
            String nonTerminal = entry.getKey();
            ArrayList<ArrayList<String>> productions = entry.getValue();
            System.out.print(nonTerminal + " -> ");
            for (ArrayList<String> production: productions) {
                for (String symbol : production) {
                    System.out.print(symbol + " ");
                }
                if (productions.indexOf(production) != productions.size() - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
    }

    public void printProductions(String nonTerminal) {
        ArrayList<ArrayList<String>> productions = this.productions.get(nonTerminal);
        System.out.print(nonTerminal + " -> ");
        for (ArrayList<String> production : productions) {
            for (String symbol : production) {
                System.out.print(symbol + " ");
            }
            if (productions.indexOf(production) != productions.size() - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();
    }

    public boolean isCFG() {
        return productionsCG.isEmpty();
    }
}
