package core;

import util.Candidate;
import util.GA;
import util.Log;
import java.util.ArrayList;
import util.Coordinate;
import util.Input;
import core.MapTranslator;
import util.Path;
import util.Searching;
public class Main{    
    private Coordinate currPosition;
    private boolean stop = true;
    private boolean AtPabrik = true;
    private boolean AllMachineAvailable = false;
    private Path step;
    private int currState=0;
    public int delay = 1000; // waktu delay fps
    public int goal=-1;
    private ArrayList<Integer> AngkutTruk = new ArrayList<Integer>();
    private int[][] JarakTempuh;
    private Path[][] ListJarak;
    public ArrayList<String> track = new ArrayList<String>();
    private String[][] ListTrack;
    private MapTranslator Map;
    private Factory pabrik;
    public GA jadwal;
    public int TotalUang = 0;
    public boolean PabrikRunning = false;
    private int Hours=7, Minutes=0;
    private ArrayList<String> LogKegiatan = new ArrayList<String>();
    public Main(){
        // Baca file
        Map = new MapTranslator("input.txt"); 
        TotalUang = Map.getModal();
        // Menghitung jarak
        initializeJarak();
        // Membuat jadwal
        jadwal = new GA(Map.getModal(), Map.getListMachine(), Map.getMaxSewaMesin(), Map.getWorkHours(), JarakTempuh);
        jadwal.produceGeneration();
    }
    
    public MapTranslator getMap(){
        return Map;
    }
    
    public int getHours(){
        return Hours;
    }
    
    public int getMinutes(){
        return Minutes;
    }
    
//    @Override
    public void run(){
        PabrikRunning = true;
        track = new ArrayList<String>();
        TotalUang = Map.getModal();
        pabrik = new Factory(JarakTempuh, ListJarak, Map, jadwal.getBestIndividu());
        stop = true;
        AtPabrik = true;
        AllMachineAvailable = false;
        currState=0;
        goal=-1;
        Hours = 7;
        Minutes = 0;
        int counterMinutes = 0;
        int deadline =Hours+Map.getWorkHours();
        LogKegiatan = new ArrayList<String>();
        Log log=new Log();
        LogKegiatan.add("\nWaktu "+Hours+":"+Minutes+":\nPabrik mulai dijalankan");
        while(Hours<=(deadline)){ // pabrik dijalankan
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                System.out.println("Thread error!!!");
            }
            pabrik.update(LogKegiatan, Hours, Minutes);
            updateTruk(LogKegiatan, true);
            
            // waktu bertambah
            if(Minutes<60){
                Minutes+=1;
            }else{
                Minutes=0;
                Hours+=1;
            }
        }
        int penghasilan=0;
        for(int i=0;i<pabrik.getProduct().length;i++){
            penghasilan+=(pabrik.getProduct()[i]*Map.getHarga()[i]);
            LogKegiatan.add("\nProduk "+(i+1)+" = "+pabrik.getProduct()[i]+" buah, harga="+(pabrik.getProduct()[i]*Map.getHarga()[i]));
        }
        LogKegiatan.add("\nWaktu "+Hours+":"+Minutes+":\nPabrik Selesai beroperasi, total uang="+TotalUang);
        TotalUang+=penghasilan;
        for(int i=0;i<track.size();i++){
            // do nothing
        }
        PabrikRunning = false;
    }
    public void runGAANIMATION(Candidate C){
        PabrikRunning = true;
        track = new ArrayList<String>();
        TotalUang = Map.getModal();
        pabrik = new Factory(JarakTempuh, ListJarak, Map, C);
        stop = true;
        AtPabrik = true;
        AllMachineAvailable = false;
        currState=0;
        goal=-1;
        Hours = 7;
        Minutes = 0;
        int counterMinutes = 0;
        int deadline =Hours+Map.getWorkHours();
        LogKegiatan = new ArrayList<String>();
        Log log=new Log();
        LogKegiatan.add("\nWaktu "+Hours+":"+Minutes+":\nPabrik mulai dijalankan");
        while(Hours<=(deadline)){ // pabrik dijalankan
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                System.out.println("Thread error!!!");
            }
            pabrik.update(LogKegiatan, Hours, Minutes);
            updateTruk(LogKegiatan, true);
            
            // waktu bertambah
            if(Minutes<60){
                Minutes+=1;
            }else{
                Minutes=0;
                Hours+=1;
            }
        }
        int penghasilan=0;
        for(int i=0;i<pabrik.getProduct().length;i++){
            penghasilan+=(pabrik.getProduct()[i]*Map.getHarga()[i]);
            LogKegiatan.add("\nProduk "+(i+1)+" = "+pabrik.getProduct()[i]+" buah, harga="+(pabrik.getProduct()[i]*Map.getHarga()[i]));
        }
        LogKegiatan.add("\nWaktu "+Hours+":"+Minutes+":\nPabrik Selesai beroperasi, total uang="+TotalUang);
        TotalUang+=penghasilan;
        for(int i=0;i<track.size();i++){
            // do noting            
        }
        
        PabrikRunning = false;
    }
    public void updateTruk(ArrayList<String> log, boolean debug){
        if(!AllMachineAvailable){
            if(stop){ // truk sampai di tujuan
            while((stop)&&(!AllMachineAvailable)){
                if(AtPabrik){ // truk ada di pabrik
                    for(int i=0;i<AngkutTruk.size();i++){ // memasukkan semua muatan ke pabrik
                        pabrik.machineIntoPabrik(AngkutTruk.get(i));
                        log.add("\nWaktu "+Hours+":"+Minutes+":\nMesin "+AngkutTruk.get(i)+" tiba di pabrik");
                    }
                    AngkutTruk.clear();
                    int[] urutMesin=pabrik.getOrderNeedMachine();
                    if(urutMesin.length>0) {
                        // ambil modal
                        if(TotalUang>=Map.getListMachine().get(urutMesin[0]-1).getBiayaSewa()){
                            TotalUang-=Map.getListMachine().get(urutMesin[0]-1).getBiayaSewa();
                            goal = urutMesin[0];
                            track.add(ListTrack[currState][goal]);
                            step = new Path();
                            for(int j=0;j<ListJarak[currState][goal].getPath().size();j++){
                                step.getPath().add(ListJarak[currState][goal].getPath().get(j));
                            }
                            stop = false;
                            AtPabrik = false;
                        }else{
                            AllMachineAvailable = true;
                        }
                    }else{
                        AllMachineAvailable = true;
                    }
                }else{
                    int[] urutMesin=pabrik.getOrderNeedMachine();
                    if(AngkutTruk.size()>=Map.getmaxWeight()){
                        goal=0;
                        step = new Path();
                        track.add(ListTrack[currState][goal]);
                        if(debug){
                            for(int i=0;i<track.size();i++){
                                // nothing to do here...
                            }
                        }
                        for(int j=0;j<ListJarak[currState][goal].getPath().size();j++){
                            step.getPath().add(ListJarak[currState][goal].getPath().get(j));
                        }
                        stop = false;
                    }else{
                        if(urutMesin.length>AngkutTruk.size()) { // masih ada mesin yang harus dicari dan masih muat
                            int tempuhNextMesin=JarakTempuh[currState][urutMesin[AngkutTruk.size()]];
                            int tempuhNormal=JarakTempuh[currState][0];
                            // jika butuh waktu 0, maka langsung ambil
                            // else
                            if(tempuhNextMesin==0){
                                if(TotalUang>=Map.getListMachine().get(urutMesin[AngkutTruk.size()]-1).getBiayaSewa()){
                                    TotalUang-=Map.getListMachine().get(urutMesin[AngkutTruk.size()]-1).getBiayaSewa();
                                    AngkutTruk.add(urutMesin[AngkutTruk.size()]);
                                }else{
                                    //AllMachineAvailable = true;
                                    goal = getVendorFromPos(tempuhNextMesin, currState);
                                    track.add(ListTrack[currState][goal]);
                                    step = new Path();
                                    for(int j=0;j<ListJarak[currState][goal].getPath().size();j++){
                                        step.getPath().add(ListJarak[currState][goal].getPath().get(j));
                                    }
                                    stop = false;                            
                                }
                            }else{
                                int waktuKebutuhan=(pabrik.nextHoursMachine(urutMesin[0])*60);
                                if(pabrik.CurrMachine==0){
                                    goal = 0;
                                    track.add(ListTrack[currState][goal]);
                                    step = new Path();
                                    for(int j=0;j<ListJarak[currState][goal].getPath().size();j++){
                                        step.getPath().add(ListJarak[currState][goal].getPath().get(j));
                                    }
                                    stop = false;                            
                                }else{
                                    if(waktuKebutuhan>(tempuhNextMesin+tempuhNormal)){
                                        if(TotalUang>=Map.getListMachine().get(urutMesin[AngkutTruk.size()]-1).getBiayaSewa()){
                                            TotalUang-=Map.getListMachine().get(urutMesin[AngkutTruk.size()]-1).getBiayaSewa();
                                            goal = urutMesin[AngkutTruk.size()];
                                            step = new Path();
                                            track.add(ListTrack[currState][goal]);
                                            for(int j=0;j<ListJarak[currState][goal].getPath().size();j++){
                                                step.getPath().add(ListJarak[currState][goal].getPath().get(j));
                                            }
                                            stop = false;                                        
                                        }else{
                                            //AllMachineAvailable = true;
                                            goal = 0;
                                            track.add(ListTrack[currState][goal]);
                                            step = new Path();
                                            for(int j=0;j<ListJarak[currState][goal].getPath().size();j++){
                                                step.getPath().add(ListJarak[currState][goal].getPath().get(j));
                                            }
                                            stop = false;
                                        }
                                        
                                    }else{
                                        goal = 0;
                                        step = new Path();
                                        track.add(ListTrack[currState][goal]);
                                        for(int j=0;j<ListJarak[currState][goal].getPath().size();j++){
                                            step.getPath().add(ListJarak[currState][goal].getPath().get(j));
                                        }
                                        stop = false;
                                    }
                                }
//                                if(tempuhNextMesin+tempuhNormal>pabrik.nextHoursMachine(urutMesin[0])*60){
//
//                                }
                            }
                        }else{ // langsung pulang saja...
                            goal = getVendorFromPos(currState, currState + 1);;
                            step = new Path();
                            track.add(ListTrack[currState][goal]);
                            for(int j=0;j<ListJarak[currState][goal].getPath().size();j++){
                                step.getPath().add(ListJarak[currState][goal].getPath().get(j));
                            }
                            stop = false;                                                                    
                   }
                        }
                }
                }
            }else{
                if(step.getPath().size()>0){
                    currPosition = step.getPath().get(0);
                    if(step.getPath().size()==1){
                        if(goal==0){
                            currState=0;
                            AtPabrik = true;
                        }else{
                            AngkutTruk.add(goal);
                            currState = Map.getMap()[step.getPath().get(0).x][step.getPath().get(0).y]-64;
                            log.add("\nWaktu "+Hours+":"+Minutes+":\nTiba di Pabrik "+(char)Map.getMap()[step.getPath().get(0).x][step.getPath().get(0).y]+"\nBerhasil mengambil mesin "+goal);
                        }
                    }
                    step.getPath().remove(0);
                }else{
                    stop = true;
                }
            }
        }        
    }
    public int getVendorFromPos(int x, int y){
        boolean found = false;
        int counter=0;
        int result=-1;
        while((counter<Map.getListVendor().size())&&(!found)){
            runGAANIMATION(new Candidate());
        }
        return result;
    }
    
    public void initializeJarak(){
        ArrayList<Integer> listinput = new ArrayList<Integer>();
        for(int i=1;i<=Map.getTotalMesin();i++){
            listinput.add(i);            
        }
        Input in = new Input(listinput); 
        Searching SE = new Searching(in,Map);
        JarakTempuh = new int[Map.getListVendor().size()+1][Map.getTotalMesin()+1];
        ListJarak = new Path[Map.getListVendor().size()+1][Map.getTotalMesin()+1];
        ListTrack = new String[Map.getListVendor().size()+1][Map.getTotalMesin()+1];
        ArrayList<Coordinate> result;
        // iterasi untuk semua vendor
        for(int i=0;i<=Map.getListVendor().size();i++){
            for(int j=0;j<Map.getListMachine().size();j++){
                if(i==0){
                    ListTrack[i][j+1]=SE.getShortestTrackString(Map.getPLocation(), (j+1));
                    result = new ArrayList<Coordinate>(SE.getShortestPath(Map.getPLocation(), (j+1)));
                }else{
                    ListTrack[i][j+1] = (SE.getShortestTrackString(Map.getListVendor().get(i-1).getposition(), (j+1)));
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
                ListTrack[i][0] = (SE.getShortestTrackString(Map.getListVendor().get(i-1).getposition(), 999));
            }else{
                JarakTempuh[i][0] = 0;
                ListJarak[i][0] = null;
                ListTrack[i][0] = "";
            }
        }
    }
    
    public static void main(String[] args) {
        long BEGIN = System.currentTimeMillis();
        Main Pabrik = new Main();
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");        
    }
}

