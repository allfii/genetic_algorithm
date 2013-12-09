package util;
import core.Machine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import core.MapTranslator;

public class GA {
    protected ArrayList<Candidate> population = new ArrayList<Candidate>(); // list kromosom
    private Random rand; // generator bilangan random
    private final int POPULATION_SIZE = 10; // ukuran populasi
    private final int PARENT_USE_PERCENT = 10; // persentase sisa kromosom induk yang masih digunakan saat terjadi seleksi alam
    private final int MAX_GENERATION = 10; // maksimal generasi yang dihasilkan
    private int GenotypeSize; // jumlah genotip dalam setiap kromosom
    private int TotalMesin;
    private int GenerationNumber=0;
    public float MutatePercent = 0.5f;
    public Fitness fitness;
    public boolean stopProducing, threadDie;
    Map<String, Integer> hashMap = new HashMap<String, Integer>();
    private ArrayList<Integer> Harga;
    // untuk log
    ArrayList<String> inisial = new ArrayList();
    ArrayList<String> seleksi = new ArrayList();
    ArrayList<String> crossOver = new ArrayList();
    ArrayList<String> mutasi = new ArrayList();
    Log log = new Log();
    DebugHandler debugging;

    public GA(int Modal, ArrayList<Machine> listMachine, int MaxSewa, int WorkHours, int[][] _JarakTempuh) {
        rand = new Random();
        initFitness(listMachine, Modal, WorkHours, _JarakTempuh, MaxSewa);
        GenotypeSize = WorkHours;
        TotalMesin = listMachine.size();
        // inisialisasi populasi awal
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Candidate kromosom = new Candidate(GenotypeSize,  TotalMesin);
            kromosom.generateGenotype(); // generate genotipnya
            population.add(kromosom);
        }
        initHandler();
    }
    
    public void initFitness(final ArrayList<Machine> ListMachine, final int Modal, final int WorkHours, final int[][] _JarakTempuh, final int MaxSewa){        
        Harga = new ArrayList<Integer>();
        fitness = new Fitness(new FitnessHandler() {
            @Override
            public int calculateFunc(Candidate Chromosom) {
                        int sum = 0;
                        if(hashMap.get(Chromosom.kromosom())==null){
                            print(Chromosom.toString());
                            int ModalAwal = Modal;
                            boolean valid = false;
                            int[] BebanKerjaHari = new int[ListMachine.size()];
                            int[] BebanKerjaPeriode = new int[ListMachine.size()];
                            int[] WaktuIstirahat = new int[ListMachine.size()];
                            int[] Product = new int[ListMachine.size()];
                            int delay = 0;
                            for(int i=0;i<ListMachine.size();i++){
                                Product[i] = 0;
                                BebanKerjaHari[i] = 0;
                                BebanKerjaPeriode[i] = 0;
                                WaktuIstirahat[i] = 0;
                                ListMachine.get(i).WaktuTempuh = 10;
                                delay+=ListMachine.get(i).WaktuTempuh;
                            }
                            int WaktuTersisa = (Chromosom.SIZE*60 - (delay))/60 ;
                            int[] mesinAvailable = new int[ListMachine.size()];
                            for(int i=0;i<MaxSewa;i++){
                                mesinAvailable[i]=0;
                            }
                            for(int i=1;i<=Chromosom.SIZE;i++){
                                boolean validAvailable = false;
                                if(WaktuTersisa>0){
                                    WaktuTersisa-=1;
                                    print("Waktu masih tersisa");
                                    if(mesinAvailable[Chromosom.getGenotype(i)]!=1){
                                        int available=0;
                                        for(int k=0;k<ListMachine.size();k++){
                                            if(mesinAvailable[k]==1){
                                                available+=1;
                                            }
                                        }
                                        if(available>=MaxSewa){
                                        }else{
                                            if(ModalAwal>=ListMachine.get(Chromosom.getGenotype(i)).getBiayaSewa()){
                                                print("Mesin "+(Chromosom.getGenotype(i)+1)+" disewa : "+ListMachine.get(Chromosom.getGenotype(i)).getBiayaSewa());
                                                ModalAwal-=ListMachine.get(Chromosom.getGenotype(i)).getBiayaSewa();
                                                print("Total uang = "+ModalAwal);                                
                                                validAvailable = true;
                                                mesinAvailable[Chromosom.getGenotype(i)]=1;
                                            }else{
                                                print("Total uang tidak mencukupi untuk disewakan");
                                                print("Mesin "+(Chromosom.getGenotype(i)+1)+" harus disewa : "+ListMachine.get(Chromosom.getGenotype(i)).getBiayaSewa());
                                            }
                                        }
                                    }else{
                                        validAvailable = true;
                                    }

                                    if(validAvailable){
                                        print("Mesin " + (Chromosom.getGenotype(i)+1)+"("+BebanKerjaHari[Chromosom.getGenotype(i)]+":"+BebanKerjaPeriode[Chromosom.getGenotype(i)]+":"+WaktuIstirahat[Chromosom.getGenotype(i)]+") revive");
                                        valid = false;
                                        if(BebanKerjaHari[Chromosom.getGenotype(i)]<ListMachine.get(Chromosom.getGenotype(i)).getBebanKerjaPerHari()){
                                            if(WaktuIstirahat[Chromosom.getGenotype(i)]<=0){
                                                if(ListMachine.get(Chromosom.getGenotype(i)).getDependency()==-1){ // tidak perlu mesin lain
                                                    valid = true;
                                                }else{
                                                    int NoProdukDibutuhkan=ListMachine.get(Chromosom.getGenotype(i)).getDependency();
                                                    print("Mesin "+(Chromosom.getGenotype(i)+1)+" perlu produk "+NoProdukDibutuhkan);
                                                    if(ListMachine.get(Chromosom.getGenotype(i)).getAmountDependency()<=Product[NoProdukDibutuhkan-1]){
                                                        valid = true;
                                                        Product[NoProdukDibutuhkan-1]-=ListMachine.get(Chromosom.getGenotype(i)).getAmountDependency();
                                                        print("produk "+(NoProdukDibutuhkan)+" berkurang menjadi = "+Product[NoProdukDibutuhkan-1]);
                                                    }else{
                                                        
                                                    }
                                                }
                                            }else{
                                                print("Mesin "+(Chromosom.getGenotype(i)+1)+" masih harus istirahat");
                                            }
                                        }else{
                                            print("Mesin "+(Chromosom.getGenotype(i)+1)+" beban kerja habis");
                                        }
                                        if(valid){
                                            BebanKerjaHari[Chromosom.getGenotype(i)]+=1;
                                            if(i==1){
                                                BebanKerjaPeriode[Chromosom.getGenotype(i)]=0;
                                            }
                                            else if((i>1)&&(Chromosom.getGenotype(i)!=Chromosom.getGenotype(i-1))){ // mesin diganti
                                                WaktuIstirahat[Chromosom.getGenotype(i-1)]=ListMachine.get(Chromosom.getGenotype(i-1)).getIstirahat();
                                                BebanKerjaPeriode[Chromosom.getGenotype(i)]=0;
                                                print("Mesin diganti menjadi "+(Chromosom.getGenotype(i)+1));
                                            }else{
                                                print("Mesin dipakai lagi");
                                                WaktuIstirahat[Chromosom.getGenotype(i)]=0;
                                            }
                                            BebanKerjaPeriode[Chromosom.getGenotype(i)]+=1;
                                            print("Mesin "+(Chromosom.getGenotype(i)+1)+" beban hari ="+BebanKerjaHari[Chromosom.getGenotype(i)]);
                                            print("Mesin "+(Chromosom.getGenotype(i)+1)+" beban periode ="+BebanKerjaPeriode[Chromosom.getGenotype(i)]);
                                            print("Mesin "+(Chromosom.getGenotype(i)+1)+" istirahat ="+WaktuIstirahat[Chromosom.getGenotype(i)]);
                                            if(BebanKerjaPeriode[Chromosom.getGenotype(i)]>ListMachine.get(Chromosom.getGenotype(i)).getBebanKerjaPerPeriode()){
                                                print("Mesin panas");
                                                Product[Chromosom.getGenotype(i)]+=(ListMachine.get(Chromosom.getGenotype(i)).getProduktivitas()*ListMachine.get(Chromosom.getGenotype(i)).getCostOverload()/100);
                                                print("Produk "+(Chromosom.getGenotype(i)+1)+" bertambah "+(ListMachine.get(Chromosom.getGenotype(i)).getProduktivitas()*ListMachine.get(Chromosom.getGenotype(i)).getCostOverload()/100));
                                            }else{
                                                Product[Chromosom.getGenotype(i)]+=ListMachine.get(Chromosom.getGenotype(i)).getProduktivitas();
                                                print("Produk "+(Chromosom.getGenotype(i)+1)+" bertambah");
                                            }
                                            print("Produk "+(Chromosom.getGenotype(i)+1)+" menjadi "+Product[Chromosom.getGenotype(i)]);
                                        }else{
                                            print("Tidak valid");                        
                                        }
                                    }else{
                                        print("Mesin lebih dari jumlah sewa atau uang modal tidak cukup");
                                    }

                                    for(int j=0;j<ListMachine.size();j++){
                                        if(j!=(Chromosom.getGenotype(i))){
                                            if(mesinAvailable[j]==1){
                                                if(WaktuIstirahat[j]>0){
                                                    WaktuIstirahat[j]-=1;
                                                }
                                                print("Mesin "+(j+1)+" harus istirahat lagi");
                                            }
                                        }
                                    }
                                }else print("Jadwal Habis");
                            }
                            sum=0;
                            for(int i=0;i<Product.length;i++){
                                sum+=(Product[i]*ListMachine.get(i).Harga);
                                print("Produk "+(i+1));
                            }
                            hashMap.put(Chromosom.kromosom(), sum);
                            print("HashMap null di "+Chromosom.kromosom()+" hasil="+sum);
                        }else{
                            sum = hashMap.get(Chromosom.kromosom());
                            print("Hashmap "+Chromosom.kromosom()+" ada");
                        }
                        print("Max Sewa "+MaxSewa);        
                        return sum;

            }
        });
    }
    public int getBestFitness(){
        return fitness.calculate(getBestIndividu());
    }
    
    private void initHandler(){
        debugging = new DebugHandler() {
            @Override
            public void printOutput(String message) {
                if (!true) System.out.println("--Debug: "+message);
            }
        };
    }
    
    protected Candidate getPopulation(int step) {
        final int maxSteps = step;
        int count = 0;
        while (count < maxSteps) {
                count++;
                produceGeneration();
        }
        return population.get(0);
    }
    
    public Candidate getBestIndividu(){
        int f1 = fitness.calculate(population.get(0));
        int f2;
        int result=0;
        for(int i=1;i<population.size();i++){
            f2 = fitness.calculate(population.get(i));
            if(f1<f2){
                f1 = f2;
                result=i;
            }
        }
        return population.get(result);
    }
    
    public void produceGeneration() {
        GenerationNumber+=1;
        for(int i=0;i<population.size();i++){
            inisial.add(population.get(i).kromosom());
        }
        ArrayList<Candidate> newPopulation = new ArrayList<Candidate>();
        while (newPopulation.size() < POPULATION_SIZE * (1.0-(PARENT_USE_PERCENT/100.0)) ) {
                int size = population.size();
                int i = rand.nextInt(size);
                int j, k, l;
                j = k = l = i;
                while (j == i)
                        j = rand.nextInt(size);
                while (k == i || k == j)
                        k = rand.nextInt(size);
                while (l == i || l == j || k == l)
                        l = rand.nextInt(size);

                Candidate c1 = population.get(i);
                Candidate c2 = population.get(j);
                Candidate c3 = population.get(k);
                Candidate c4 = population.get(l);

                int f1 = fitness.calculate(c1);
                int f2 = fitness.calculate(c2);
                int f3 = fitness.calculate(c3);
                int f4 = fitness.calculate(c4);

                Candidate w1, w2;

                // proses seleksi awal
                // pembandingan nilai fitness, yang menang yang diambil
                if (f1 > f2)
                        w1 = c1;		
                else 
                        w1 = c2;			
                if (f3 > f4)
                        w2 = c3;			
                else  
                        w2 = c4;
                
                // calon generasi baru
                Candidate child1, child2;
                seleksi.add(w1.kromosom());
                seleksi.add(w2.kromosom());
                
                // proses crossover
                Candidate[] childs = newChilds(w1,w2);
                child1 = childs[0];
                child2 = childs[1];
                crossOver.add(child1.kromosom());
                crossOver.add(child2.kromosom());

                // proses mutasi
                boolean m1 = rand.nextFloat() <= MutatePercent;
                boolean m2 = rand.nextFloat() <= MutatePercent;
                if(m1){
                        mutate(child1);
                        mutasi.add(child1.kromosom());
                }
                if(m2){
                        mutate(child2);
                        mutasi.add(child2.kromosom());
                }
                
                // proses seleksi
                boolean isChild1Good = fitness.calculate(child1) >= fitness.calculate(w1);
                boolean isChild2Good = fitness.calculate(child2) >= fitness.calculate(w2);

                newPopulation.add( isChild1Good ? child1 : w1);
                newPopulation.add( isChild2Good ? child2 : w2);
        }

        // ganti kromosom induk yang lama dengan yang baru dengan persentase yang telah ditentukan
        int j = (int)(POPULATION_SIZE*PARENT_USE_PERCENT/100.0);
        for (int i = 0; i < j; i++) {
            newPopulation.add( population.get(i));
        }		
        population=newPopulation;	
        try {
            log.writeLog(inisial, seleksi, crossOver, mutasi, "log.txt");
        } catch (IOException ex) {
            Logger.getLogger(GA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Candidate[] newChilds(Candidate c1, Candidate c2)
    {		
            Candidate child1 = new Candidate(GenotypeSize,  TotalMesin);
            Candidate child2 = new Candidate(GenotypeSize,  TotalMesin);

            int potong = rand.nextInt(GenotypeSize);
            for (int i = 0; i < GenotypeSize; i++) {
                    if(i<potong){						
                            child1.genotype[i] = c1.genotype[i];
                            child2.genotype[i] = c2.genotype[i];
                    }
                    else //penyilangan
                    {
                            child1.genotype[i] = c2.genotype[i];
                            child2.genotype[i] = c1.genotype[i];
                    }
            }
            return new Candidate[]{child1,child2} ;		
    }	
    public void mutate(Candidate kromosom) {
        int i = rand.nextInt(GenotypeSize);
        kromosom.genotype[i] = rand.nextInt(TotalMesin); // tukar
    }
    public static void main(String[] args) {
        MapTranslator Map = new MapTranslator("input1.txt"); 
        // Menghitung jarak dan memasukkannya ke list
        ArrayList<Integer> listinput = new ArrayList<Integer>();
        for(int i=1;i<=Map.getTotalMesin();i++){
            listinput.add(i);            
        }
        Input in = new Input(listinput);
        Searching SE = new Searching(in,Map);
        int[][] JarakTempuh = new int[Map.getListVendor().size()+1][Map.getTotalMesin()+1];
        Path[][] ListJarak = new Path[Map.getListVendor().size()+1][Map.getTotalMesin()+1];
        ArrayList<Coordinate> result;
        for(int i=0;i<=Map.getListVendor().size();i++){
            for(int j=0;j<Map.getListMachine().size();j++){
                if(i==0){
                    result = new ArrayList<Coordinate>(SE.getShortestPath(Map.getPLocation(), (j+1)));
                }else{
                    result = new ArrayList<Coordinate>(SE.getShortestPath(Map.getListVendor().get(i-1).getposition(), (j+1)));                    
                }
                result.remove(0);
                JarakTempuh[i][j+1] = result.size();
                ListJarak[i][j+1] = new Path(result);                        
            }
            if(i!=0){
                result = new ArrayList<Coordinate>(SE.getShortestPath(Map.getListVendor().get(i-1).getposition(), 999));
                result.remove(0);
                JarakTempuh[i][0] = result.size();
                ListJarak[i][0] = new Path(result);                
            }else{
                JarakTempuh[i][0] = 0;
                ListJarak[i][0] = null;                                
            }
        }
        // Membuat Jadwal
        GA jadwal = new GA(Map.getModal(), Map.getListMachine(), Map.getMaxSewaMesin(), Map.getWorkHours(), JarakTempuh);
        jadwal.getPopulation(1);
        
    }
    
    public void print(String message){
        debugging.printOutput(message);
    }
}
