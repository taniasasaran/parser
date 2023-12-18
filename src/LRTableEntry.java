import java.util.Map;

record LRTableEntry(Map<String, State> targetStates, Action actionType) {

}
