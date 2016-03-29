import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class KnowledgeBase {
    private ArrayList<Clause> knowledge; // order based on input
    private PriorityQueue<Clause> clauseSelectionOrder; // order based on size of clauses,
                                                        // ordering can be changed in
                                                        // Clause.compareTo()
    private ArrayList<Pair> resolved; // clauses that have been resolved together
    
    public KnowledgeBase() {
        this.knowledge = new ArrayList<Clause>();
        this.clauseSelectionOrder = new PriorityQueue<Clause>();
        this.resolved = new ArrayList<Pair>();
    }
    
    public void add(Clause c) {
        this.knowledge.add(c);
        this.clauseSelectionOrder.add(c);
    }
    
    public boolean prove() {
        boolean proven = false;
        while(!proven) {
            Iterator<Clause> outer = clauseSelectionOrder.iterator();
            while(outer.hasNext()) {
                Clause first = outer.next();
                Iterator<Clause> inner = clauseSelectionOrder.iterator();
                while(inner.hasNext()) {
                    Clause second = inner.next();
                    
                    if(Clause.resolvable(first, second) && !isResolved(first, second)) {
                        Clause child = resolve(first, second);
                        knowledge.add(child);
                        clauseSelectionOrder.add(child);
                        resolved.add(new Pair(this.knowledge.indexOf(first) + 1,
                                this.knowledge.indexOf(second) + 1));
                        if(child.isFail()) {
                            return true;
                        }
                        // reset iterator since a new value has been inserted
                        outer = clauseSelectionOrder.iterator();
                        inner = clauseSelectionOrder.iterator();
                        break;
                    }
                }
                if(proven) {
                    break;
                }
            }
            
            // no clauses that are resolvable
            return false;
            
        }
        
        return true;
    }
    
    public Clause resolve(Clause x, Clause y) {
        HashSet<Literal> xLits = x.getLiterals();
        HashSet<Literal> yLits = y.getLiterals();
        HashSet<Literal> combi = new HashSet<>();
        combi.addAll(xLits);
        combi.addAll(yLits);
        
        Clause c = null;
        for(Literal xLit : xLits) {
            for(Literal yLit : yLits) {
                if(xLit.getOpposite().equals(yLit)) {
                    combi.remove(xLit);
                    combi.remove(yLit);
                }
            }
        }
        
        c = new Clause(combi);
        c.setParents(this.knowledge.indexOf(x) + 1, this.knowledge.indexOf(y) + 1);
        return c;
    }
    
    
    // print the path to the solution
    public void printSolutionTree() {
        Clause last = this.knowledge.get(this.knowledge.size() - 1);
        TreeMap<Integer, Clause> tree = new TreeMap<Integer, Clause>();
        tree.put(this.knowledge.size(), last);
        this.addParents(last, tree);
        
        for(int index : tree.keySet()) {
            System.out.println(String.format("%d. %s", index, tree.get(index)));
        }
        System.out.println(String.format("Size of final clause set: %d", knowledge.size()));
    }
    
    
    public void printSolutionTree(BufferedWriter bw) throws IOException {
        Clause last = this.knowledge.get(this.knowledge.size() - 1);
        TreeMap<Integer, Clause> tree = new TreeMap<Integer, Clause>();
        tree.put(this.knowledge.size(), last);
        this.addParents(last, tree);
        
        for(int index : tree.keySet()) {
            bw.write(String.format("%d. %s\n", index, tree.get(index)));
        }
        bw.write(String.format("Size of final clause set: %d\n", knowledge.size()));
    }
    
    // recursively adds the parents of a clause to the solution tree
    public void addParents(Clause clause, TreeMap<Integer, Clause> tree) {
        if(clause.getParents() == null) {
            return;
        } else {
            int[] parents = clause.getParents();
            
            Clause mother = this.knowledge.get(parents[0] - 1);
            tree.put(parents[0], mother);
            this.addParents(mother, tree);
            
            Clause father = this.knowledge.get(parents[1] - 1);
            tree.put(parents[1], father);
            this.addParents(father, tree);
        }
    }
    
    
    public boolean isResolved(Clause first, Clause second) {
        Pair n = new Pair(this.knowledge.indexOf(first) + 1, this.knowledge.indexOf(second) + 1);
        for(Pair p : this.resolved) {
            if(n.equals(p)) {
                return true;
            }
        }
        return false;
    }
    
    private class Pair {
        private int x;
        private int y;
        
        
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public boolean equals(Pair other) {
            return (this.x == other.x && this.y == other.y)
                    || (this.x == other.y && this.y == other.x);
        }
    }
    
}
