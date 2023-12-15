import java.util.HashMap;
import java.util.Map;

public class ParserOutput {
    private Map<Integer, Map<String, LRTableEntry>> table;

    public ParserOutput() {
        this.table = new HashMap<>();
    }

    public void addAction(int state, String symbol, LRTableEntry action) {
        table.computeIfAbsent(state, k -> new HashMap<>()).put(symbol, action);
    }

    public LRTableEntry getAction(int state, String symbol) {
        return table.getOrDefault(state, new HashMap<>()).get(symbol);
    }

    public void addGoto(int state, String symbol, int nextState) {
        table.computeIfAbsent(state, k -> new HashMap<>()).put(symbol, new LRTableEntry(nextState, LRTableEntry.ActionType.GOTO));
    }

    public int getGoto(int state, String symbol) {
        LRTableEntry entry = table.getOrDefault(state, new HashMap<>()).get(symbol);
        if (entry != null && entry.getActionType() == LRTableEntry.ActionType.GOTO) {
            return entry.getTargetState();
        }
        return -1; // Indicates an error or absence
    }
}

class LRTableEntry {
    public enum ActionType {
        SHIFT, REDUCE, ACCEPT, GOTO
    }

    private ActionType actionType;
    private int targetState;

    public LRTableEntry(int targetState, ActionType actionType) {
        this.targetState = targetState;
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public int getTargetState() {
        return targetState;
    }
}
