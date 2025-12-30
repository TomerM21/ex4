package cfg;

import java.util.*;
import ir.*; // all the ir commands
public class Graphblock
{
    private final int id;//unique block id
    private final List<IrCommand> commands;//all the ir commands in this block
    private final Set<Graphblock> successors;//out  edges
    private final Set<Graphblock> predecessors;//in edges 
    private Map<String, State> in;//state in-like in lecture
    private Map<String, State> out;// state out-like in lecture

    public Graphblock(int id) {//builder
        this.id = id;
        this.commands = new ArrayList<>();
        this.successors = new HashSet<>();
        this.predecessors = new HashSet<>();
        this.in = new HashMap<>();
        this.out = new HashMap<>();
    }

    /**
     * USE: Variables used before being written in this block.
     * Logic: Iterate through commands. If we see a read and it hasn't 
     * been defined in this block yet, it's a used variable.
     */
    public Set<String> getUse() {
        Set<String> useSet = new HashSet<>();
        Set<String> defInBlock = new HashSet<>();
        for (IrCommand cmd : commands) {
            for (String readVar : cmd.getReadVariables()) {  // Check variables read by this command
                if (!defInBlock.contains(readVar)) {
                    useSet.add(readVar);
                }
            }
            defInBlock.addAll(cmd.getWriteVariables());// Add variables written by this command to the local def
        }
        return useSet;
    }


    public Set<String> getDef() {// variables defined in this block
        Set<String> defSet = new HashSet<>();
        for (IrCommand cmd : commands) {
            defSet.addAll(cmd.getWriteVariables());
        }
        return defSet;
    }

    /**
     * compute OUT from IN.
     * Formula: OUT[B] = gen[B] U (IN[B] - kill[B])
     * In our case: Any variable defined in this block becomes INITIALIZED.
     * Everything else stays as it was in the IN set.
     */
    // public boolean transfer() {
    //     Map<String, State> newOut = new HashMap<>(this.in);

    //     // Gen: Anything defined in this block is now INITIALIZED
    //     for (String varName : getDef()) {
    //         newOut.put(varName, State.INITIALIZED);
    //     }

    //     if (!newOut.equals(this.out)) {
    //         this.out = newOut;
    //         return true; // Return true if the OUT set changed
    //     }
    //     return false;
    // }

   
    /**
     * Updates the OUT set based on the IN set and the commands in the block.
     * It simulates execution line-by-line to handle dependencies correctly.
     */
    public boolean transfer() {
        // 1. Start simulation with the state at the block entry (IN set)
        Map<String, State> currentState = new HashMap<>(this.in);

        // 2. Iterate commands sequentially
        for (IrCommand cmd : commands) {
            
            // Step A: Check inputs (READS)
            // If ANY input variable is UNINITIALIZED, the operation is "poisoned" and the new value is UNINITIALIZED
            boolean usesUninit = false;
            for (String readVar : cmd.getReadVariables()) {
                // Default to UNINITIALIZED if not found (safety)
                if (currentState.getOrDefault(readVar, State.UNINITIALIZED) == State.UNINITIALIZED) {
                    usesUninit = true;
                    break;
                }
            }

            // Step B: Update outputs (WRITES)
            for (String writeVar : cmd.getWriteVariables()) {
                if (usesUninit) {
                    // Propagate "Bad" state: Result depends on uninitialized data
                    currentState.put(writeVar, State.UNINITIALIZED);
                } else {
                    // Generate "Good" state: Assigned a constant or fully initialized value
                    currentState.put(writeVar, State.INITIALIZED);
                }
            }
        }

        // 3. Update OUT set and report if it changed
        // This triggers the chaotic iteration to continue if we found new info
        if (!currentState.equals(this.out)) {
            this.out = currentState;
            return true; 
        }
        return false;
    }


    //basic functions mainlly for fullgraph
    public void addCommand(IrCommand cmd) {
        commands.add(cmd);
    }

    public void addSuccessor(Graphblock block) {
        successors.add(block);
        block.predecessors.add(this);
    }
    
    public int getId() { return id; }
    public List<IrCommand> getCommands() { return commands; }
    public Set<Graphblock> getSuccessors() { return successors; }
    public Set<Graphblock> getPredecessors() { return predecessors; }
    public Map<String, State> getIn() { return in; }
    public Map<String, State> getOut() { return out; }
    public void setIn(Map<String, State> in) { this.in = in; }
    public void setOut(Map<String, State> out) { this.out = out; }
    @Override
    public String toString() {
        return "Block " + id + " | Commands: " + commands.size() + 
               " | Successors: " + successors.stream().map(b -> b.id).toList();
    }
}