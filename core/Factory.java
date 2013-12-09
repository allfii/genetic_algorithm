package core;

import util.Candidate;
import java.util.ArrayList;
import core.MapTranslator;
import util.Path;

public class Factory {
    private MapTranslator Map;
    public Candidate Jadwal;
    private int PenunjukJadwal=0;
    public int CurrMachine = 0;
    private int LastMachine = 0;
    private int CurrLifeTimeMachine = 0;
    private boolean[] AvailableMachine;
    private int[] UseMachine; // penggunaan total jam tiap mesin
    private int[] UseMachineInPeriode; // penggunaan total jam tiap mesin
    private int[] RestMachine; // I
    private int[] MinutesRestMachine; // menit istirahat mesin
    private int[] Product; // total keuntungan
    public Factory(int[][] jarak, Path[][] listJarak, MapTranslator map, Candidate jadwal){
        Map = map;
        Jadwal = jadwal;
        AvailableMachine = new boolean[Map.getListMachine().size()];
        UseMachine = new int[Map.getListMachine().size()];
        UseMachineInPeriode = new int[Map.getListMachine().size()];
        RestMachine = new int[Map.getListMachine().size()];
        MinutesRestMachine = new int[Map.getListMachine().size()];
        for(int i=0;i<Map.getListMachine().size();i++){
            UseMachine[i]=0;
            UseMachineInPeriode[i]=0;
            AvailableMachine[i]=false;
            RestMachine[i]=0;
            MinutesRestMachine[i]=0;
        }
        Product = new int[Map.getListMachine().size()];
        for (int i=0;i<Map.getListMachine().size();i++){
            Product[i] = 0; // Jumlah product        
        }
    }

    public void update(ArrayList<String> log, int jam, int menit){
        boolean MachineOneHour = false;
        // mengoperasikan mesin yang sedang bekerja //
        if(CurrMachine!=0) {// ada mesin yang harus dikerjakan
            if(CurrLifeTimeMachine>0){
                CurrLifeTimeMachine-=1;
            }else{ // waktu mesin bekerja sudah satu jam
                UseMachine[CurrMachine-1]+=1;
                if(UseMachineInPeriode[CurrMachine-1]>=Map.getListMachine().get(CurrMachine-1).getBebanKerjaPerPeriode()){
                    int jumlahProduk=(Map.getListMachine().get(CurrMachine-1).getProduktivitas()*Map.getListMachine().get(CurrMachine-1).getCostOverload()/100);
                    Product[CurrMachine-1] += jumlahProduk;
                    log.add("\nWaktu "+jam+":"+menit+":\nDidapatkan produk "+CurrMachine+" sebanyak "+jumlahProduk+" setelah mesin overload");
                }else{
                    Product[CurrMachine-1] += Map.getListMachine().get(CurrMachine-1).getProduktivitas();
                    log.add("\nWaktu "+jam+":"+menit+":\nDidapatkan produk "+CurrMachine+" sebanyak "+Map.getListMachine().get(CurrMachine-1).getProduktivitas());
                }
                log.add("\nWaktu "+jam+":"+menit+":\nTotal produk "+CurrMachine+" sekarang "+Product[CurrMachine-1]);
                LastMachine = CurrMachine;
                RestMachine[CurrMachine-1] = Map.getListMachine().get(CurrMachine-1).getIstirahat();
                CurrMachine = 0;
                MachineOneHour = true;
                PenunjukJadwal+=1;
            }
        }
        if(CurrMachine==0){ // tidak ada mesin yang harus dikerjakan
            boolean adaMesinYangBekerja = false;
            if(PenunjukJadwal<Jadwal.SIZE){
                    int noMachine = Jadwal.getGenotype(PenunjukJadwal+1);
                    if(UseMachine[noMachine]<Map.getListMachine().get(noMachine).getBebanKerjaPerHari()){
                            if(AvailableMachine[noMachine]){
                                // hidupkan mesin
                                boolean valid = false;
                                if(Map.getListMachine().get(noMachine).getDependency()==-1){
                                    valid = true;
                                }else{
                                    int produkYgDibutuhkan=Map.getListMachine().get(noMachine).getDependency();
                                    if(Product[produkYgDibutuhkan-1]>=Map.getListMachine().get(noMachine).getAmountDependency()){
                                        Product[produkYgDibutuhkan-1]-=Map.getListMachine().get(noMachine).getAmountDependency();
                                        valid = true;
                                    }else{ // produk tidak cukup
                                        
                                    }
                                }
                                
                                if(valid){
                                    int temp = noMachine+1;
                                    if(MachineOneHour){
                                        if(LastMachine==temp){ // mesin digunakan lagi
                                            CurrMachine = noMachine+1;
                                            CurrLifeTimeMachine = 60;
                                            adaMesinYangBekerja = true;
                                            RestMachine[CurrMachine-1] = 0;
                                            UseMachineInPeriode[CurrMachine-1]+=1;
                                            log.add("\nWaktu "+jam+":"+menit+":\nMesin "+CurrMachine+" akan dihidupkan lagi");
                                        }else{ // mesin diganti
                                            if(RestMachine[noMachine]<=0){ // mesin siap kerja
                                                CurrMachine = noMachine+1;
                                                log.add("\nWaktu "+jam+":"+menit+":\nMesin "+CurrMachine+" akan dihidupkan");
                                                CurrLifeTimeMachine = 60;
                                                adaMesinYangBekerja = true;
                                                UseMachineInPeriode[CurrMachine-1]=0;
                                            }else { // mesin harus istirahat
                                            }
                                        }
                                    }else{// mesin diganti
                                        if(RestMachine[noMachine]<=0){ // mesin siap kerja                                        
                                            CurrMachine = noMachine+1;
                                            log.add("\nWaktu "+jam+":"+menit+":\nMesin "+CurrMachine+" akan dihidupkan");
                                            CurrLifeTimeMachine = 60;
                                            adaMesinYangBekerja = true;
                                        }else { // mesin harus istirahat
                                            PenunjukJadwal+=1;
                                        }
                                    }                                    
                                }else{
                                    PenunjukJadwal+=1;
                                }
                            }else {
                                int SewaMachine =0;
                                for(int i=0;i<Map.getListMachine().size();i++){
                                    if((AvailableMachine[i])){
                                        SewaMachine +=1;
                                    }
                                }
                            }
                            
                    }else{
                        PenunjukJadwal+=1;
                    }
            }
            if(PenunjukJadwal<Jadwal.SIZE){
            }else{
            }
        }

        // mengistirahatkan mesin yang tidak bekerja //
        for(int i=0;i<Map.getListMachine().size();i++){
            if(AvailableMachine[i]){
                if((i+1)!=CurrMachine){
                    MinutesRestMachine[i]+=1;
                    if(MinutesRestMachine[i]>60){
                        MinutesRestMachine[i] = 0;
                        if(RestMachine[i]>0){
                            RestMachine[i]-=1;
                        }                    
                    }
                }
            }
        }
    }
    public void machineIntoPabrik(int noMachine){
        if(!AvailableMachine[noMachine-1]){
            int SewaMachine =0;
            for(int i=0;i<Map.getListMachine().size();i++){
                if(((i+1)!=noMachine)&&(AvailableMachine[i])){
                    SewaMachine +=1;
                }
            }
            if(SewaMachine<Map.getMaxSewaMesin()){ // masih bisa sewa
                AvailableMachine[noMachine-1]=true;
            }else{
            }
        }
    }
    public int[] getOrderNeedMachine(){
        int counter=0;
        int kebutuhan=0;
        int sewa =0;
        for(int i=0;i<Map.getListMachine().size();i++){
            if((AvailableMachine[i])){
                sewa +=1;
            }
        }
        int[] result = new int[Map.getMaxSewaMesin()-sewa];
        for(int i=0;i<result.length;i++){
            result[i]=-1;
        }
        while((sewa<Map.getMaxSewaMesin())&&(counter<Jadwal.SIZE)){
            if(!AvailableMachine[Jadwal.getGenotype(counter+1)]){
                boolean SudahAda = false;
                for(int i=0;i<result.length;i++){
                    if(result[i]==Jadwal.getGenotype(counter+1)){
                        SudahAda = true;
                    }
                }
                if(!SudahAda){
                    sewa+=1;
                    result[kebutuhan] = Jadwal.getGenotype(counter+1);
                    kebutuhan+=1;
                }
            }
            counter+=1;
        }
        int[] result_fix = new int[kebutuhan];
        for(int i=0;i<kebutuhan;i++){
            result_fix[i]=result[i]+1;
        }
        return result_fix;
        
    }
    public int nextHoursMachine(int noMachine){
        boolean found = false;
        int result=PenunjukJadwal;
        while((result<Jadwal.SIZE)&&(!found)){
            if(Jadwal.getGenotype(result+1)==(noMachine-1)){
                found = true;
            }else result+=1;
        }
        if(found) return result;else return -1;
    }
    public int[] getProduct(){
        return Product;
    }
}
