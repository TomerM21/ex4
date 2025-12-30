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
    public boolean transfer() {
        Map<String, State> newOut = new HashMap<>(this.in);

        // Gen: Anything defined in this block is now INITIALIZED
        for (String varName : getDef()) {
            newOut.put(varName, State.INITIALIZED);
        }

        if (!newOut.equals(this.out)) {
            this.out = newOut;
            return true; // Return true if the OUT set changed
        }
        return false;
    }

   
    //basic functions mainlly for fullgraph
    public void addCommand(IrCommand cmd) {
        commands.add(cmd);
    }
    public void addSuccessor(CFGBlock block) {
        successors.add(block);
        block.predecessors.add(this);
    }
    public int getId() { return id; }
    public List<IrCommand> getCommands() { return commands; }
    public Set<CFGBlock> getSuccessors() { return successors; }
    public Set<CFGBlock> getPredecessors() { return predecessors; }
     public Map<String, State> getIn() { return in; }
    public void setIn(Map<String, State> in) { this.in = in; }
    public Map<String, State> getOut() { return out; }
    @Override
    public String toString() {
        return "Block " + id + " | Commands: " + commands.size() + 
               " | Successors: " + successors.stream().map(b -> b.id).toList();
    }
}