import java.util.HashSet;

public class Clause {
    private HashSet<Literal> literals; // elements redundancies, order isn't important
    private int[] parents;
    
    
    public Clause() {
        this.literals = new HashSet<Literal>();
        this.parents = null;
    }
    
    public Clause(HashSet<Literal> combi) {
        this.literals = combi;
        parents = null;
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        for(Literal lit : literals) {
            sb.append(lit.toString());
            sb.append(" ");
        }
        if(this.parents == null) {
            return String.format("%s {}", sb.toString());
        } else {
            return String.format("%s {%d, %d}", sb.toString(), parents[0], parents[1]);
        }
    }
    
    public void setParents(int mother, int father) {
        this.parents = new int[] {mother, father};
    }
    
    public void addLiteral(Literal lit) {
        this.literals.add(lit);
    }
    
    public int[] getParents() {
        return this.parents;
    }
    
    public static boolean resolvable(Clause x, Clause y) {
        for(Literal lit : x.literals) {
            if(y.literals.contains(lit.getOpposite())) {
                return true;
            }
        }
        return false;
    }
    
    public HashSet<Literal> getLiterals() {
        return this.literals;
    }
}
