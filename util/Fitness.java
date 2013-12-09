package util;

public class Fitness {
    private FitnessHandler fitnessInterface;
    
    public Fitness(){ 
        functionSetup(null);
    }
    
    public Fitness(FitnessHandler handler){ 
        fitnessInterface = handler;
    }
    
    public void functionSetup(FitnessHandler handler){
        if (handler==null) functionSetup();
        else fitnessInterface = handler;
    }
    
    public void functionSetup(){
        fitnessInterface = new FitnessHandler() { @Override public int calculateFunc(Candidate Chromosom) { throw new UnsupportedOperationException("Not supported yet."); } };
    }
    
    public int compareTo(Candidate Chromosom1, Candidate Chromosom2) {
        int fitness1 = calculate(Chromosom1);
        int fitness2 = calculate(Chromosom2);
        if (fitness1 < fitness2)
            return 1;
        else if (fitness1 > fitness2)
            return -1;
        else
            return 0;
    }
    
    public int calculate(Candidate Chromosom){
        return fitnessInterface.calculateFunc(Chromosom);
    }
}
