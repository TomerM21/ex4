package cfg;

import ir.*; 
import java.util.*;

public class Fullcfggraph {
    private List<Graphblock> blocks;
    private Graphblock head;
    private List<IrCommand> rawirCommands;

    public Fullcfggraph(List<IrCommand> irCommands) {
        this.blocks = new ArrayList<>();
        this.rawirCommands = irCommands;
    }

    public static Fullcfggraph buildFromIR(List<IrCommand> irCommands) {
        Fullcfggraph fullcfggraph = new Fullcfggraph(irCommands);
        fullcfggraph.partitionIntoBlocks();
        fullcfggraph.buildControlFlowEdges();
        return fullcfggraph;
    }

  
    private void partitionIntoBlocks() {//Pass 1: Identify "Leaders" to split IR into Basic Blocks
        if (rawirCommands.isEmpty()) return;

        Set<Integer> leaders = new TreeSet<>();
        leaders.add(0); // Rule 1: First command is a leader

        for (int i = 0; i < rawirCommands.size(); i++) {
            IrCommand cmd = rawirCommands.get(i);
            if (cmd.isJump()) { // goto or if-goto
                // Rule 2: Target of a jump is a leader
                int targetIndex = findTargetIndex(cmd.getJumpLabel());
                if (targetIndex != -1) leaders.add(targetIndex);
                // Rule 3: Command immediately following a jump is a leader
                if (i + 1 < rawirCommands.size()) leaders.add(i + 1);
            }
        }

        // Create blocks based on leader indices
        List<Integer> leaderList = new ArrayList<>(leaders);
        for (int i = 0; i < leaderList.size(); i++) {
            int start = leaderList.get(i);
            int end = (i + 1 < leaderList.size()) ? leaderList.get(i + 1) : rawirCommands.size();
            
            graphblock block = new Graphblock(i);
            for (int j = start; j < end; j++) {
                block.addCommand(rawirCommands.get(j));
            }
            blocks.add(block);
        }
        this.head = blocks.get(0);
    }

     
     
    private void buildControlFlowEdges() {//connecting the blocks 
        for (int i = 0; i < blocks.size(); i++) {
            Graphblock current = blocks.get(i);
            IrCommand lastCmd = current.getCommands().get(current.getCommands().size() - 1);

            if (lastCmd.isUnconditionalJump()) {
                // Connect only to the target block
                current.addSuccessor(findBlockByLabel(lastCmd.getJumpLabel()));
            } else if (lastCmd.isConditionalJump()) {
                // Connect to target AND next sequential block
                current.addSuccessor(findBlockByLabel(lastCmd.getJumpLabel()));
                if (i + 1 < blocks.size()) current.addSuccessor(blocks.get(i + 1));
            } else {
                // Normal command: just connect to next sequential block
                if (i + 1 < blocks.size()) current.addSuccessor(blocks.get(i + 1));
            }
        }
    }

    /**
     * Dataflow Analysis: The Chaotic Iterations (Worklist) Algorithm.
     */
    public void runDataflowAnalysis(Set<String> allVariables) {
        initializeWorklist(allVariables);
        Queue<Graphblock> worklist = new LinkedList<>(blocks);

        while (!worklist.isEmpty()) {
            Graphblock block = worklist.poll();

            // Meet Operator: IN[B] = Intersection of OUT[Predecessors]
            // For variable initialization, a variable is INITIALIZED only if 
            // it is INITIALIZED on ALL paths.
            if (!block.getPredecessors().isEmpty()) {
                Map<String, State> mergedIn = new HashMap<>(block.getIn());
                for (String var : allVariables) {
                    boolean allInit = true;
                    for (Graphblock pred : block.getPredecessors()) {
                        if (pred.getOut().get(var) == State.UNINITIALIZED) {
                            allInit = false;
                            break;
                        }
                    }
                    mergedIn.put(var, allInit ? State.INITIALIZED : State.UNINITIALIZED);
                }
                block.setIn(mergedIn);
            }

            // Transfer: Compute OUT from IN
            if (block.transfer()) {
                // If OUT changed, re-add all successors to worklist
                worklist.addAll(block.getSuccessors());
            }
        }
    }

    private void initializeWorklist(Set<String> allVariables) {
        for (Graphblock block : blocks) {
            Map<String, State> initialMap = new HashMap<>();
            for (String var : allVariables) {
                initialMap.put(var, State.UNINITIALIZED);
            }
            block.setIn(new HashMap<>(initialMap));
            block.getOut().putAll(initialMap);
        }
    }

    // Helper: Find index of a label in raw commands
    private int findTargetIndex(String label) { /* Implementation depends on your IR */ return 0; }
    
    // Helper: Find which block starts with a specific label
    private Graphblock findBlockByLabel(String label) { /* Implementation logic */ return null; }
}