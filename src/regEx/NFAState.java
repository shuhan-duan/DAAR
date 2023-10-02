package regEx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NFAState {
	private  boolean isFinal;
	private Map<String, Set<NFAState>> transitionsMap;
	private int id;
	private static int stateCounter = 0;
	

	public NFAState() {
		this.setTransitionsMap(new HashMap<>());
		this.id = stateCounter++ ;
	}
	
	public void addTransition(String symbol, NFAState destination) {
		this.getTransitionsMap().computeIfAbsent(symbol, k -> new HashSet<>()).add(destination);		
	}
	
	public String getName() {
	    return "S" + id;
	}

	@Override
	public String toString() {
	    StringBuilder result = new StringBuilder();
	    result.append("State ").append(id).append(isFinal() ? " (Final)" : "").append(":\n");
	    
	    for (Map.Entry<String, Set<NFAState>> entry : getTransitionsMap().entrySet()) {
	        String symbol = entry.getKey();
	        for (NFAState destination : entry.getValue()) {
	            result.append("\t").append(symbol).append(" -> State ").append(destination.id).append("\n");
	        }
	    }
	    
	    return result.toString();
	}

	public Map<String, Set<NFAState>> getTransitionsMap() {
		return transitionsMap;
	}

	public void setTransitionsMap(Map<String, Set<NFAState>> transitionsMap) {
		this.transitionsMap = transitionsMap;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean s) {
	    this.isFinal = s;
	}

	public Set<NFAState> episilonClosure() {
		
		return null;	
	}

}


