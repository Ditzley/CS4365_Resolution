import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Resolver {
    
    static boolean saveSolution = true; // change to false to prevent saving to file
    
    public static void main(String[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException("Expected 1 argument, got " + args.length);
        }
        
        KnowledgeBase kb = new KnowledgeBase();
        
        String inFile = args[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(inFile));
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
            kb.printSolutionTree();
        } else {
            System.out.println("Failure");
        }
        
        if(saveSolution) {
            String outFile = inFile.replace(".in", ".out");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
                
                kb.printSolutionTree(bw);
                
                bw.close();
                
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
