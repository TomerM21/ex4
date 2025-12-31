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


    // ==========================================================
    //                 Dataflow Analysis Logic
    // ==========================================================

    public Set<String> runDataflowAnalysis() {
        if (blocks.isEmpty()) return Collections.emptySet();
        // 1. Collect ALL variables (including Temps) to ensure we track everything
        Set<String> allVars = collectAllVariables();

        // 2. Initialize Worklist with Optimistic Assumption (INITIALIZED)
        //    This allows loops to converge to the correct "Must-Be-Initialized" set.
        initializeWorklist(allVars);

        // 3. Initialize Worklist with ONLY the First Block
        //    We let the changes propagate from the entry point.
        Queue<Graphblock> worklist = new LinkedList<>();
        if (this.head != null) {
            worklist.add(this.head);
        }

        // 4. Chaotic Iteration
        while (!worklist.isEmpty()) {
            Graphblock block = worklist.poll();

            // Merge Predecessors (Intersection Rule)
            // A variable is INITIALIZED only if it is INITIALIZED in ALL predecessors.
            if (!block.getPredecessors().isEmpty()) {
                Map<String, State> newIn = new HashMap<>();
                
                for (String var : allVars) {
                    boolean isUninitOnAnyPath = false;
                    for (Graphblock pred : block.getPredecessors()) {
                        // If ANY predecessor has it UNINITIALIZED, it enters as UNINITIALIZED
                        if (pred.getOut().getOrDefault(var, State.UNINITIALIZED) == State.UNINITIALIZED) {
                            isUninitOnAnyPath = true;
                            break;
                        }
                    }
                    newIn.put(var, isUninitOnAnyPath ? State.UNINITIALIZED : State.INITIALIZED);
                }
                block.setIn(newIn);
            }

            // Transfer: Calculate OUT from IN
            // If OUT changes, add successors to worklist
            if (block.transfer()) {
                for (Graphblock succ : block.getSuccessors()) {
                    if (!worklist.contains(succ)) {
                        worklist.add(succ);
                    }
                }
            }
        }

        // 5. Final Scan: Report Errors
        //    We re-run the transfer logic one last time to capture the specific
        //    moment a "bad" variable is read.
        return collectBadVariables();
    }

    private Set<String> collectBadVariables() {
        Set<String> badVars = new HashSet<>();
        
        for (Graphblock block : blocks) {
            // Start simulation from the block's computed IN state
            Map<String, State> current = new HashMap<>(block.getIn());
            
            for (IrCommand cmd : block.getCommands()) {
                // Special case: Allocate always creates UNINITIALIZED variable (handles shadowing)
                if (cmd instanceof IrCommandAllocate) {
                    for (String writeVar : cmd.getWriteVariables()) {
                        current.put(writeVar, State.UNINITIALIZED);
                    }
                    continue; // Skip normal processing
                }
                
                // Step A: Reporting - Check named variable reads (not temps)
                for (String read : cmd.getReadVariables()) {
                    if (current.getOrDefault(read, State.UNINITIALIZED) == State.UNINITIALIZED) {
                        // Only report source variables (filter out Temps and Labels)
                        if (!read.startsWith("Temp_") && !read.startsWith("Label_")) {
                            badVars.add(read);
                        }
                    }
                }
                
                // Step B: Update State - Check ALL reads (variables AND temps)
                boolean usesUninit = false;
                
                // Check named variable reads
                for (String read : cmd.getReadVariables()) {
                    if (current.getOrDefault(read, State.UNINITIALIZED) == State.UNINITIALIZED) {
                        usesUninit = true;
                        break; 
                    }
                }
                
                // Check temp reads
                if (!usesUninit) {
                    for (String readTemp : cmd.getReadTemps()) {
                        if (current.getOrDefault(readTemp, State.UNINITIALIZED) == State.UNINITIALIZED) {
                            usesUninit = true;
                            break;
                        }
                    }
                }
                
                // Update written variables
                for (String write : cmd.getWriteVariables()) {
                    current.put(write, usesUninit ? State.UNINITIALIZED : State.INITIALIZED);
                }
                
                // Update written temps
                for (String writeTemp : cmd.getWriteTemps()) {
                    current.put(writeTemp, usesUninit ? State.UNINITIALIZED : State.INITIALIZED);
                }
            }
        }
        return badVars;
    }

    private void initializeWorklist(Set<String> allVariables) {
        // Initialize all blocks to Optimistic (INITIALIZED)
        for (Graphblock block : blocks) {
            Map<String, State> optimisticMap = new HashMap<>();
            for (String var : allVariables) {
                optimisticMap.put(var, State.INITIALIZED);
            }
            block.setIn(new HashMap<>(optimisticMap));
            block.setOut(new HashMap<>(optimisticMap));
        }

        // Initialize Entry block to Pessimistic (UNINITIALIZED)
        if (head != null) {
            for (String var : allVariables) {
                head.getIn().put(var, State.UNINITIALIZED);
            }
        }
    }

    private Set<String> collectAllVariables() {
        Set<String> vars = new HashSet<>();
        for (IrCommand cmd : rawirCommands) {
            vars.addAll(cmd.getReadVariables());
            vars.addAll(cmd.getWriteVariables());
            vars.addAll(cmd.getReadTemps());
            vars.addAll(cmd.getWriteTemps());
        }
        return vars;
    }
  
    
   
    // public Set<String> runDataflowAnalysis(Set<String> allVariables) {
    //     initializeWorklist(allVariables);
    //     Queue<Graphblock> worklist = new LinkedList<>(blocks);
    //     while (!worklist.isEmpty()) {
    //         Graphblock block = worklist.poll();// takes and removes the element from the queue head
    //         if (!block.getPredecessors().isEmpty())
    //         {
    //             Map<String, State> mergedIn = new HashMap<>(block.getIn());
    //             for (String var : allVariables) {
    //                 boolean allInit = true;
    //                 for (Graphblock pred : block.getPredecessors()) {
    //                     if (pred.getOut().get(var) == State.UNINITIALIZED) {
    //                         allInit = false;
    //                         break;
    //                     }
    //                 }
    //                 if(allInit){ mergedIn.put(var, State.INITIALIZED);}
    //                 else       { mergedIn.put(var, State.UNINITIALIZED);}
                   
    //             }
    //             block.setIn(mergedIn);
    //         }

           
    //         if (block.transfer()) {//transfet=true-->out changed so we need to reprocess
    //             worklist.addAll(block.getSuccessors());
    //         }
    //     }
    // }

    // private void initializeWorklist(Set<String> allVariables) {
    //     for (Graphblock block : blocks) {
    //         Map<String, State> initialMap = new HashMap<>();
    //         for (String var : allVariables) {
    //             initialMap.put(var, State.UNINITIALIZED);
    //         }
    //         block.setIn(new HashMap<>(initialMap));
    //         block.getOut().putAll(initialMap);
    //     }
    // }

    // ==========================================================
    //               Graph Construction Helpers
    // ==========================================================

    private void partitionIntoBlocks() { // Identify "Leaders" to split IR into Basic Blocks
        if (rawirCommands.isEmpty()) return;

        Set<Integer> leaders = new TreeSet<>(); // Using TreeSet to keep leaders sorted
        leaders.add(0); // Rule 1: First command is a leader

        for (int i = 0; i < rawirCommands.size(); i++) {
            IrCommand cmd = rawirCommands.get(i);
            if (cmd.isJump()) { // tomerm added to our ircommand this method
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
            
            Graphblock block = new Graphblock(i);
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

            if (lastCmd.isUnconditionalJump()) { // Connect only to the target block
                Graphblock targetBlock = findBlockByLabel(lastCmd.getJumpLabel());
                if (targetBlock != null) current.addSuccessor(targetBlock);
                // else: print error - label doesnt exist

            } else if (lastCmd.isConditionalJump()) { // Connect to target AND next sequential block
                Graphblock targetBlock = findBlockByLabel(lastCmd.getJumpLabel());
                if (targetBlock != null) current.addSuccessor(targetBlock);
                // else: print error - label doesnt exist
                if (i + 1 < blocks.size()) current.addSuccessor(blocks.get(i + 1));

            } else { // Normal command: just connect to next sequential block
                if (i + 1 < blocks.size()) current.addSuccessor(blocks.get(i + 1));
            }
        }
    }

    // Helper: Find index of a label in raw commands
    private int findTargetIndex(String label) {
        if (label == null) return -1;

        for (int i = 0; i < rawirCommands.size(); i++) {
            IrCommand cmd = rawirCommands.get(i);
            if (cmd instanceof IrCommandLabel) {
                if (((IrCommandLabel) cmd).getLabelName().equals(label)) {
                    return i;
                }
            }
        }
        // Should not happen in a correct program
        return -1;
    }
    
    // Helper: Find which block starts with a specific label
    private Graphblock findBlockByLabel(String label) {
        if (label == null) return null;
        if (blocks == null) return null;

        for (Graphblock block : blocks) {
            // Check the first command of the block
            if (!block.getCommands().isEmpty()) {
                IrCommand firstCmd = block.getCommands().get(0);
                if (firstCmd instanceof IrCommandLabel) {
                    if (((IrCommandLabel) firstCmd).getLabelName().equals(label)) {
                        return block;
                    }
                }
            }
        }
        return null;
    }
}