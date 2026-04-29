package tm;
import java.io.*;
import java.util.*;
/**
 * Main driver class for the Turing Machine simulator.
 *
 * Reads the TM description and input string from a file,
 * constructs the Turing Machine, and runs the simulation.
 *
 * @author YourName
 */
public class TMSimulator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java tm.TMSimulator <inputfile>");
            System.exit(1);
        }
        String filename = args[0];
        try {
            TM tm = parseInputFile(filename);
            String output = tm.run();
            System.out.println(output);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
    /**
     * Parses the Turing Machine definition and input string from a file.
     *
     * @param filename Path to the TM definition file
     * @return Initialized Turing Machine
     * @throws IOException if file cannot be read
     */
    private static TM parseInputFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() || lines.isEmpty()) { // keep empty last line
                    lines.add(line);
                }
            }
        }
        int numStates = Integer.parseInt(lines.get(0));
        int numSymbols = Integer.parseInt(lines.get(1));
        // transitions = (numStates - 1) * (numSymbols + 1)
        int numTransitions = (numStates - 1) * (numSymbols + 1);
        Map<Integer, Map<Integer, TMTransition>> transitions = new HashMap<>();
        int lineIndex = 2;
        for (int state = 0; state < numStates - 1; state++) {
            transitions.put(state, new HashMap<>());
            for (int symbol = 0; symbol <= numSymbols; symbol++) {
                String[] parts = lines.get(lineIndex++).split(",");
                int nextState = Integer.parseInt(parts[0]);
                int writeSymbol = Integer.parseInt(parts[1]);
                char move = parts[2].charAt(0);
                transitions.get(state).put(symbol, new TMTransition(nextState, writeSymbol, move));
            }
        }
        // input string is the last line (may be blank)
        String input = (lineIndex < lines.size()) ? lines.get(lineIndex) : "";
        return new TM(numStates, numSymbols, transitions, input);
    }
}
File 2 — tm/TM.java
Represents the Turing Machine itself.

java


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
