import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

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
    
    // naive, simply prints all clauses in order
    // should change to print only those that were necessary
    public void print() {
        int i = 1;
        for(Clause c : knowledge) {
            System.out.println(String.format("%d. %s", i, c.toString()));
            i++;
        }
        System.out.println(String.format("Size of final clause set: %d", knowledge.size()));
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
