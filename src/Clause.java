import java.util.HashSet;

public class Clause implements Comparable<Clause> {
    private HashSet<Literal> literals; // elements redundancies, order isn't important
    private int[] parents;
    private int size;
    
    public Clause() {
        this.literals = new HashSet<Literal>();
        this.size = 0;
        this.parents = null;
    }
    
    public Clause(HashSet<Literal> literals) {
        this.literals = literals;
        this.size = this.literals.size();
        this.parents = null;
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        for(Literal lit : this.literals) {
            sb.append(lit.toString());
            sb.append(" ");
        }
        String name = sb.toString();
        if(this.size == 0) {
            name = "False";
        }
        if(this.parents == null) {
            return String.format("%s {}", name);
        } else {
            return String.format("%s {%d, %d}", name, this.parents[0], this.parents[1]);
        }
    }
    
    public void setParents(int mother, int father) {
        this.parents = new int[] {mother, father};
    }
    
    public void addLiteral(Literal lit) {
        this.literals.add(lit);
        this.size++;
    }
    
    public int[] getParents() {
        return this.parents;
    }
    
    public static boolean resolvable(Clause x, Clause y) {
        for(Literal xLit : x.literals) {
            for(Literal yLit : y.literals) {
                if(xLit.getOpposite().equals(yLit)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public HashSet<Literal> getLiterals() {
        return this.literals;
    }
    
    public boolean isFail() {
        return this.size == 0;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Clause other) {
        return Integer.compare(this.size, other.size);
    }
    
    @Override
    public boolean equals(Object other) {
        Clause that = (Clause) other;
        return this.literals.equals(that.literals);
    }
}
