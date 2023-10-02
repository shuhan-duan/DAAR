package regEx;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NFA {
	public static final String EPSILON = "epsilon";
	
	private NFAState startState;
	private NFAState endState;
	
	

	public NFA() {

	}

	
	public NFA(NFAState startState, NFAState endState) {
	    this.startState = startState;
	    this.endState = endState;
	}
	
	public NFA fromRegExTree(RegExTree tree) {
        switch (tree.root) {
            case RegEx.DOT:
                // Handle dot (.)
            	return handleCharacter(tree);
            case RegEx.ETOILE:
                // Handle star (*)
            	return handleEtoile(fromRegExTree(tree.subTrees.get(0)));
            case RegEx.CONCAT:
                // Handle concatenation
            	return handleConcat(fromRegExTree(tree.subTrees.get(0)), fromRegExTree(tree.subTrees.get(1)));
            case RegEx.ALTERN:
                // Handle alternation (|)
            	return handleAltern(fromRegExTree(tree.subTrees.get(0)), fromRegExTree(tree.subTrees.get(1)));
            default:
                // Handle basic character
            	return handleCharacter(tree);
        }
    }
	
	private NFA handleCharacter(RegExTree tree) {
	    NFAState start = new NFAState();
	    NFAState end = new NFAState();
	    end.setFinal(true); 
	    start.addTransition(Character.toString((char)tree.root), end);
	    return new NFA(start, end);
	}

	private NFA handleConcat(NFA r1, NFA r2) {
	    r1.endState.addTransition(EPSILON, r2.startState);
	    r1.endState.setFinal(false);
	    return new NFA(r1.startState, r2.endState);
	}
	
	private NFA handleEtoile(NFA r) {
	    NFAState newStart = new NFAState();
	    NFAState newEnd = new NFAState();
	    newEnd.setFinal(true); 
	    
	    newStart.addTransition(EPSILON, r.startState);
	    newStart.addTransition(EPSILON, newEnd);
	    r.endState.addTransition(EPSILON, r.startState);
	    r.endState.addTransition(EPSILON, newEnd);
	    r.endState.setFinal(false);
	    
	    return new NFA(newStart, newEnd);
	}

	private NFA handleAltern(NFA r1, NFA r2) {
	    NFAState newStart = new NFAState();
	    NFAState newEnd = new NFAState();
	    newEnd.setFinal(true);
	    
	    newStart.addTransition(EPSILON, r1.startState);
	    newStart.addTransition(EPSILON, r2.startState);
	    r1.endState.addTransition(EPSILON, newEnd);
	    r2.endState.addTransition(EPSILON, newEnd);
	    r1.endState.setFinal(false);
	    r2.endState.setFinal(false);
	    
	    return new NFA(newStart, newEnd);
	}


	
	@Override
	public String toString() {
	    Set<NFAState> visited = new HashSet<>();
	    StringBuilder dotBuilder = new StringBuilder();

	    dotBuilder.append("digraph NFA {\n");
	    dotBuilder.append("    rankdir=LR;\n");
	    dotBuilder.append("    node [shape = circle];\n");

	    recursiveDotPrint(startState, visited, dotBuilder);

	    dotBuilder.append("}\n");
	    
	    return dotBuilder.toString();
	}

	private void recursiveDotPrint(NFAState state, Set<NFAState> visited, StringBuilder dotBuilder) {
	    if (visited.contains(state)) {
	        return;
	    }
	    
	    if (state.isFinal()) {
	        dotBuilder.append("    ").append(state.getName()).append(" [shape = doublecircle];\n");
	    }

	    visited.add(state);

	    for (Map.Entry<String, Set<NFAState>> entry : state.getTransitionsMap().entrySet()) {
	        String symbol = entry.getKey();
	        for (NFAState destination : entry.getValue()) {
	            dotBuilder.append("    ").append(state.getName()).append(" -> ").append(destination.getName()).append(" [label=\"").append(symbol).append("\"];\n");
	            recursiveDotPrint(destination, visited, dotBuilder);
	        }
	    }
	}
}
