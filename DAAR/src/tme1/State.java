package tme1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class State {
	private  boolean isFinal;
	private Map<String, Set<State>> transitionsMap;
	private int id;
	private static int stateCounter = 0;
	

	public State() {
		this.setTransitionsMap(new HashMap<>());
		this.id = stateCounter++ ;
	}
	
	public void addTransition(String symbol, State destination) {
		this.getTransitionsMap().computeIfAbsent(symbol, k -> new HashSet<>()).add(destination);		
	}
	
	public String getName() {
	    return "S" + id;
	}

	@Override
	public String toString() {
	    StringBuilder result = new StringBuilder();
	    result.append("State ").append(id).append(isFinal() ? " (Final)" : "").append(":\n");
	    
	    for (Map.Entry<String, Set<State>> entry : getTransitionsMap().entrySet()) {
	        String symbol = entry.getKey();
	        for (State destination : entry.getValue()) {
	            result.append("\t").append(symbol).append(" -> State ").append(destination.id).append("\n");
	        }
	    }
	    
	    return result.toString();
	}

	public Map<String, Set<State>> getTransitionsMap() {
		return transitionsMap;
	}

	public void setTransitionsMap(Map<String, Set<State>> transitionsMap) {
		this.transitionsMap = transitionsMap;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean s) {
	    this.isFinal = s;
	}


}


