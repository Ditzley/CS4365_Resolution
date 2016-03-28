
public class Literal {
    private String name;
    private boolean negated;
    
    public Literal(String name, boolean negated) {
        this.name = name;
        this.negated = negated;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isNegated() {
        return this.negated;
    }
    
    public Literal getOpposite() {
        return new Literal(this.name, !this.negated);
    }
    
    @Override
    public String toString() {
        if(negated) {
            return "~" + this.name;
        } else {
            return this.name;
        }
    }
}
