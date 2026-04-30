package tm;
/**
 * Represents one transition in the TM transition function.
 *
 * @author YourName
 */
public class TMTransition {
    public final int nextState;
    public final int writeSymbol;
    public final char move; // 'L' or 'R'
    /**
     * Constructs a TM transition.
     *
     * @param nextState next state after transition
     * @param writeSymbol symbol to write on the tape
     * @param move direction to move the head (L or R)
     */
    public TMTransition(int nextState, int writeSymbol, char move) {
        this.nextState = nextState;
        this.writeSymbol = writeSymbol;
        this.move = move;
    }
}
