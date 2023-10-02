package regEx;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DFAState {
	private static int stateCounter = 0;
	
	private int id;
    private Set<NFAState> nfaStates;  
    private boolean isFinal;
    private Map<String, DFAState> transitions;
	
	public DFAState(Set<NFAState> nfaStates) {
		this.id = stateCounter++;
        this.nfaStates = nfaStates;
        this.transitions = new HashMap<>();		
	}
	
	public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    public Map<String, DFAState> getTransitions() {
        return transitions;
    }

    public void addTransition(String symbol, DFAState destination) {
        this.transitions.put(symbol, destination);
    }

    public DFAState getTransition(String symbol) {
        return this.transitions.get(symbol);
    }
    
    public int getId() {
        return id;
    }

    public Set<NFAState> getNfaStates() {
        return nfaStates;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DFAState dfaState = (DFAState) obj;
        return nfaStates.equals(dfaState.nfaStates);
    }

    @Override
    public int hashCode() {
        return nfaStates.hashCode();
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("DFAState ").append(id).append(isFinal ? " (Final)" : "").append(":\n");

        for (Map.Entry<String, DFAState> entry : transitions.entrySet()) {
            String symbol = entry.getKey();
            DFAState destination = entry.getValue();
            result.append("\t").append(symbol).append(" -> DFAState ").append(destination.getId()).append("\n");
        }

        return result.toString();
    }
}
