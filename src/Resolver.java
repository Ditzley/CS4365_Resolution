import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Resolver {
    
    public static void main(String[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException("Expected 1 argument, got " + args.length);
        }
        
        KnowledgeBase kb = new KnowledgeBase();
        
        String file = args[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null) {
                Clause clause = new Clause();
                
                String strLits[] = line.split(" ");
                for(String lit : strLits) {
                    boolean neg = false;
                    if(lit.contains("~")) {
                        neg = true;
                        lit = lit.replace("~", "");
                    }
                    
                    Literal literal = new Literal(lit.trim(), neg);
                    
                    clause.addLiteral(literal);
                }
                kb.add(clause);
            }
            
            br.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        boolean proven = kb.prove();
        if(proven) {
            kb.print();
        } else {
            System.out.println("Failure");
        }
    }
    
}
