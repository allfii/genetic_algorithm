package util;
import core.MapTranslator;
import java.util.ArrayList;
public class BFS{
    private Track currSolution; //tempat menyimpan urutan jalur
    private ArrayList<Solution> SolutionList; //hasil utama
    private ArrayList<Solution> FirstSolutionList; //hasil utama
    private ArrayList<RefinedSolution> refinedSolutionList;
    private ArrayList<Integer> currItem;
    private Coordinate startPoint; //lokasi awal pencarian dimulai
    private Coordinate currPoint; //penunjuk lokasi truk sekarang
    private ArrayList<Integer> currTruckItem; //list barang yang dibawa oleh truk
    private ArrayList<Integer> currListItem; //list barang yang perlu dicari (999 jika target adalah P)
    private ArrayList<Integer> _currListItem;
    private MapTranslator Situation; //menggambarkan abstraksi map, jika ingin mendapatkan matriks map, gunakan Situation.getmap()
    private ArrayList<BFSQueue> Queue; //antrian BFS
    private ArrayList<Coordinate> visitedArray; //daftar node yg sudah visited
    public BFS(Input inputan,MapTranslator Map){
        SolutionList = new ArrayList<Solution>();
        FirstSolutionList = new ArrayList<Solution>();
        refinedSolutionList = new ArrayList<RefinedSolution>();
        currSolution = new Track("");
        currTruckItem = new ArrayList<Integer>();
        Situation = Map;
        currListItem = new ArrayList<Integer>(inputan.getlistbarang());
        currListItem.add(999); //pasti mencari jalur ke Pabrik juga dari setiap posisi
    }
    public void doSearch(){
        /* selama list item belum habis, lakukan :
         * cek queue terdepan, buat cabang yg allowed, masukkan koordinat cabang ke dalam queue beserta jalurnya,
         * majukan currPoint ke queue selanjutnya, cek apakah goal atau bukan, jika ya, kurangi list item, jika list item
         * kosong, berarti pencarian selesai, jika tidak, 
         * ulangi pencarian dengan queue baru dimulai dari titik tersebut (jika truk masih mampu) atau kembali ke P (dengan
         * memanfaatkan jalur yg disimpan) jika truk sudah penuh, dan visitedArray yg baru, jika tidak, ulangi langkah :
         * buat cabang yg allowed, majukan currPoint ke queue selanjutnya...
         *
         */
        for(int N=0;N<=Situation.getListVendor().size();N++){
            int Z = N - 1;
            currSolution = new Track("");
            currTruckItem.clear();
            _currListItem = new ArrayList<Integer>(currListItem);
            if(Z!=-1){
                startPoint = new Coordinate(Situation.getListVendor().get(Z).getposition());
                currPoint = new Coordinate(startPoint);
            }
            Queue.clear();
            Queue.add(new BFSQueue(currSolution,currPoint));
            visitedArray.clear();
            visitedArray.add(currPoint);
            
            while(Queue.isEmpty() == false && _currListItem.isEmpty() == false){
                for(int i=0;i<=3;i++){
                    if(isAllowed(i)){
                        Track _currSolution = new Track(currSolution.addJalur(i).getJalur());
                        Queue.add(new BFSQueue(_currSolution,currPoint.move(i)));
                        visitedArray.add(currPoint.move(i));
                    }
                }
                Queue.remove(0);
                if(Situation.getMap()[currPoint.x][currPoint.y] != '.'){
                    if(Situation.getMap()[currPoint.x][currPoint.y] == 'P' && _currListItem.contains(999)){
                        Solution tempSol;
                        if(Z==-1){
                            tempSol = new Solution(999,'P',Situation.getMap()[currPoint.x][currPoint.y],createCoordinatResult(currSolution.getJalur(),startPoint),currSolution.getJalur());
                        }
                        else{
                            tempSol = new Solution(999,Situation.getListVendor().get(Z).getName(),Situation.getMap()[currPoint.x][currPoint.y],createCoordinatResult(currSolution.getJalur(),startPoint),currSolution.getJalur());
                        }
                        SolutionList.add(tempSol);
                        int idxmesin = _currListItem.indexOf(999);
                        _currListItem.remove(idxmesin);
                    }
                    else{
                        for(int i=0;i<=Situation.getListVendor().size()-1;i++){
                            if(Situation.getMap()[currPoint.x][currPoint.y] == Situation.getListVendor().get(i).getName()/* && currWeight < maxWeight*/){
                                int z = 0;
                                while(z <= Situation.getListVendor().get(i).getlistMesin().size()-1 /*&& currWeight < maxWeight*/){
                                    for(int k=0;k<=_currListItem.size()-1;k++){
                                        if(Situation.getListVendor().get(i).getlistMesin().get(z) == _currListItem.get(k)){
                                            int item = _currListItem.get(k);
                                            Solution tempSol;
                                            if(Z==-1){
                                                tempSol = new Solution(item,'P',Situation.getMap()[currPoint.x][currPoint.y],createCoordinatResult(currSolution.getJalur(),startPoint),currSolution.getJalur());
                                            }
                                            else{
                                                tempSol = new Solution(item,Situation.getListVendor().get(Z).getName(),Situation.getMap()[currPoint.x][currPoint.y],createCoordinatResult(currSolution.getJalur(),startPoint),currSolution.getJalur());
                                            }
                                            SolutionList.add(tempSol);
                                            int idxmesin = _currListItem.indexOf(item);
                                            _currListItem.remove(idxmesin);
                                        }
                                    }
                                    z++;
                                }
                            }
                        }
                    }
                }
                if(Queue.isEmpty() == false){
                    currPoint = new Coordinate(Queue.get(0).Coordinat);
                    currSolution = Queue.get(0).Trek;
                }
            }
        }
    }
    
    public void initSearching(){
        startPoint = new Coordinate(Situation.getPLocation());
        currPoint = new Coordinate(Situation.getPLocation());
        Queue = new ArrayList<BFSQueue>();
        Queue.add(new BFSQueue(currSolution,currPoint));
        visitedArray = new ArrayList<Coordinate>();
        visitedArray.add(currPoint);
    }
    
    public boolean isAllowed(int i){
        boolean value = true;
           Coordinate _currPoint = new Coordinate(currPoint.move(i));
            //batasan-batasan, jika lewat dimensi map atau menabrak # atau sudah visited
            if(_currPoint.x < Situation.getSizeX()
                    && _currPoint.x >= 0 && _currPoint.y < Situation.getSizeY() && _currPoint.y >= 0){
                //jika di dalam & visited
                if(visitedArray.isEmpty() == false){
                    for(int j=0;j<=visitedArray.size()-1;j++){
                        if(_currPoint.isEqual(visitedArray.get(j))){
                            value = false;
                            break;
                        }
                    }
                }
                //pager
                if(Situation.getMap()[_currPoint.x][_currPoint.y] == '#'){
                    value = false;
                }
            }
            else{
                value = false;
            }
        
        return value;
    }

    public ArrayList<Solution> getSolution(){
        return SolutionList;
    }
    
    //method untuk menyusun CoordinatResult dari posisi awal P dan currSolution yang terbentuk
    public ArrayList<Coordinate> createCoordinatResult(String jalur, Coordinate lokasi){
        ArrayList<Coordinate> tempArray = new ArrayList<Coordinate>();
        Coordinate tempCoor = new Coordinate(lokasi);
        tempArray.add(tempCoor);
        if(jalur.isEmpty() == false){
            for(int i=0;i<=jalur.length()-1;i++){
                Coordinate newCoor = new Coordinate(tempCoor.move(jalur.charAt(i) - 48));
                tempCoor = newCoor;
                tempArray.add(tempCoor);
            }
        }
        return tempArray;
    }
    
    public void finalSolution(){
        //tambahkan asal P ke refined dulu
        for(int a=0;a<=SolutionList.size()-1;a++){
            if(SolutionList.get(a).getasal() == 'P'){
                refinedSolutionList.add(new RefinedSolution(999,SolutionList.get(a).getmesin(),SolutionList.get(a).getlistcoor()));
            }
        }
        for(int i=0;i<=currListItem.size()-1;i++){
            for(int j=0;j<=Situation.getListVendor().size()-1;j++){
                for(int k=0;k<=Situation.getListVendor().get(j).getlistMesin().size()-1;k++){
                    if(currListItem.get(i) == Situation.getListVendor().get(j).getlistMesin().get(k)){
                        //mesin ada di vendor tersebut, cari dalam solusi
                        for(int l=0;l<=SolutionList.size()-1;l++){
                            if(SolutionList.get(l).getasal() == Situation.getListVendor().get(j).getName())
                            {
                                //ditemukan
                                refinedSolutionList.add(new RefinedSolution(currListItem.get(i),SolutionList.get(l).getmesin(),SolutionList.get(l).getlistcoor()));
                            }
                        }
                    }
                }
            }
        }
        //reduksi solusi dengan jalur terpendek saja
        for(int b=0;b<=refinedSolutionList.size()-1;b++){
            //asal dan tujuan mesin yg sama
            if(refinedSolutionList.get(b).getmesinasal() == refinedSolutionList.get(b).getmesintujuan()){
                refinedSolutionList.remove(b);
            }
        }
        //cari yang terpendek
        ArrayList<Integer> listdelete = new ArrayList<Integer>();
        for(int i=0;i<=currListItem.size()-1;i++){
            int min = 999;
            int idxmin = 0;
            ArrayList<Integer> tempList = new ArrayList<Integer>(currListItem.remove(i));
            for(int j=0;j<=tempList.size()-1;j++){
                for(int k=0;k<=refinedSolutionList.size()-1;k++){
                    if(refinedSolutionList.get(k).getmesinasal() == currListItem.get(i) && refinedSolutionList.get(k).getmesintujuan() == tempList.get(j)
                            && refinedSolutionList.get(k).getlistcoor().size() >= min){
                        listdelete.add(k);
                    }
                    else{
                        min = refinedSolutionList.get(k).getlistcoor().size();
                        idxmin = k;
                        listdelete.add(idxmin);
                    }
                }
            }
        }
        int minus = 0;
        for(int i=0;i<=listdelete.size()-1;i++){
            refinedSolutionList.remove(listdelete.get(i) - minus);
            minus++;
        }
    }

}
