import java.util.ArrayList;
import java.util.HashSet;

public class KnowledgeBase {
    private ArrayList<Clause> knowledge;
    
    public KnowledgeBase() {
        this.knowledge = new ArrayList<Clause>();
    }
    
    public void add(Clause c) {
        this.knowledge.add(c);
    }
    
    public boolean prove() {
        boolean proven = false;
        while(!proven) {
            for(Clause x : knowledge) {
                for(Clause y : knowledge) {
                    if(Clause.resolvable(x, y)) {
                        Clause child = resolve(x, y);
                        knowledge.add(child);
                        if(child.getLiterals().isEmpty()) {
                            proven = true;
                            break;
                        }
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
        for(Literal lit : xLits) {
            if(yLits.contains(lit.getOpposite())) {
                combi.remove(lit);
                combi.remove(lit.getOpposite());
                break;
            }
        }
        
        c = new Clause(combi);
        c.setParents(knowledge.indexOf(x), knowledge.indexOf(y));
        return c;
    }
    
    public void print() {
        int i = 0;
        for(Clause c : knowledge) {
            System.out.println(String.format("%d. %s", i, c.toString()));
            i++;
        }
        System.out.println(String.format("Size of final clause set: %d", knowledge.size()));
    }
}
