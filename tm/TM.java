package tm;
import java.util.*;
/**
 * Represents a deterministic bi-infinite Turing Machine.
 * Handles tape, head movement, and transitions.
 *
 * @author YourName
 */
public class TM {
    private final int numStates;
    private final int numSymbols;
    private final int startState = 0;
    private final int haltState;
    private final Map<Integer, Map<Integer, TMTransition>> transitions;
    private final Map<Integer, Integer> tape = new HashMap<>();
    private int headPosition = 0;
    private int currentState = 0;
    /**
     * Constructs a new Turing Machine with loaded transitions.
     *
     * @param numStates total number of states
     * @param numSymbols number of input symbols in Σ
     * @param transitions transition function map
     * @param input initial input string
     */
    public TM(int numStates, int numSymbols, Map<Integer, Map<Integer, TMTransition>> transitions, String input) {
        this.numStates = numStates;
        this.numSymbols = numSymbols;
        this.haltState = numStates - 1;
        this.transitions = transitions;
        // Initialize tape with input string (starting at position 0)
        for (int i = 0; i < input.length(); i++) {
            tape.put(i, Character.getNumericValue(input.charAt(i)));
        }
    }
    /**
     * Runs the Turing Machine until the halting state is reached.
     *
     * @return String of visited tape cells after halting
     */
    public String run() {
        int leftmostVisited = 0;
        int rightmostVisited = 0;
        while (currentState != haltState) {
            int readSymbol = tape.getOrDefault(headPosition, 0);
            TMTransition trans = transitions.get(currentState).get(readSymbol);
            // write symbol
            tape.put(headPosition, trans.writeSymbol);
            // move head
            if (trans.move == 'L') {
                headPosition--;
            } else {
                headPosition++;
            }
            // track visited region
            leftmostVisited = Math.min(leftmostVisited, headPosition);
            rightmostVisited = Math.max(rightmostVisited, headPosition);
            // next state
            currentState = trans.nextState;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = leftmostVisited; i <= rightmostVisited; i++) {
            sb.append(tape.getOrDefault(i, 0));
        }
        return sb.toString();
    }
}
