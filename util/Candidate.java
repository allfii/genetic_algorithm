package util;
import core.Machine;
import java.util.ArrayList;
import java.util.Random;
public class Candidate implements Comparable<Candidate> {
        public int SIZE; // jumlah genotip setiap kromosom
        public int[] genotype; // berupa boolean true sebagai bit 1 dan false sebagai bit 0
        private Random rand = new Random(); // generator bilangan random
        private int TotalMachine;
        private ArrayList<Integer> DummyGenotype, RefillGenotype;
        public Candidate() {
            SIZE = 18;
            genotype = new int[SIZE];			
            TotalMachine = 5;
        }
        public Candidate(int _size, int _MACHINE){
            SIZE = _size; 
            genotype = new int[SIZE];	
            TotalMachine = _MACHINE;
        }
        public int getGenotype(int index){
            return genotype[index-1];
        }
        public void generateGenotype(){
            for (int i = 0; i < genotype.length; i++) {
                genotype[i] = rand.nextInt(TotalMachine); // generate boolean dengan kemungkinan random randomChance
            }
        }
        public String kromosom(){
            StringBuilder kromosom = new StringBuilder();
            for (int i = 0; i < genotype.length; i++) {			
                kromosom.append(genotype[i]); // nilai true menjadi bit 1 dan false menjadi bit 0
            }
            return kromosom.toString();
        }
        @Override
        public String toString(){
                return "gene="+kromosom();
        }
    public static void main(String[] args) {
        ArrayList<Machine> mesin = new ArrayList<Machine>();
        mesin.add(new Machine(5, 2, 3, 20, 0, 20, 200000, -2));
        mesin.add(new Machine(6, 2, 2, 40, 20, 30, 400000, 1));
        mesin.add(new Machine(8, 3, 1, 30, 0, 25, 350000,  -2));
        mesin.add(new Machine(8, 2, 1, 30, 20, 20, 350000, 3));
        mesin.add(new Machine(7, 3, 1, 25, 0, 20, 300000, -2));
    }

    @Override
    public int compareTo(Candidate o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}