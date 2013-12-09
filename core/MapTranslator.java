package core;
import util.Coordinate;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MapTranslator {
    //atribut
    private char[][] map;
    private int sizeX;
    private int sizeY;
    private int PLocationX;
    private int PLocationY;
    private int jumlahMesin;
    private int maxWeight;
    private int jumlahVendor;
    private ArrayList<Vendor> ListVendor;
    private int Modal;// $
    private int WorkHours; // H
    private int MaxSewaMesin; // N
    private int[] Harga;
    private ArrayList<Machine> ListMesin= new ArrayList<Machine>(); // 
    //method
    //konstruktor
    public MapTranslator(String namafile){
        sizeX = 0;
        sizeY = 0;
        PLocationX = -999;
        PLocationY = -999;
        jumlahMesin = 0;
        maxWeight = 0;
        jumlahVendor = 0;
        ListVendor = new ArrayList<Vendor>();
        try{
            Scanner scanner = new Scanner(new FileReader(namafile));
            int i = 1;
            String value = scanner.next();
            Modal = Integer.parseInt(value);
            WorkHours = Integer.parseInt(scanner.next());
            jumlahMesin = Integer.parseInt(scanner.next());
            MaxSewaMesin = Integer.parseInt(scanner.next());
            maxWeight = Integer.parseInt(scanner.next());
            for(int j=0;j<jumlahMesin;j++){
                Machine mesin = new Machine();
                mesin.setBebanKerjaPerHari(Integer.parseInt(scanner.next()));
                mesin.setBebanKerjaPerPeriode(Integer.parseInt(scanner.next()));
                mesin.setIstirahat(Integer.parseInt(scanner.next()));
                mesin.setProduktivitas(Integer.parseInt(scanner.next()));
                mesin.setAmountDependency(Integer.parseInt(scanner.next()));
                mesin.setCostOverload(Integer.parseInt(scanner.next()));
                mesin.setBiayaSewa(Integer.parseInt(scanner.next()));
                mesin.setDependency(Integer.parseInt(scanner.next()));
                ListMesin.add(mesin);
            }
        } 
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        try{
            Scanner scanner = new Scanner(new FileReader(namafile));
            int i = 1;
            while(scanner.hasNextLine() && i<=(jumlahMesin + 1)){
                scanner.nextLine();
                i++;
            }
            if(scanner.hasNextLine()){
                String x = scanner.nextLine();
                jumlahVendor = Integer.parseInt(x);
            }
            for(int k=1;k<=jumlahVendor;k++){
                char nama = ' ';
                ArrayList<Integer> listtemp = new ArrayList<Integer>();
                if(scanner.hasNextLine()){
                    nama = scanner.nextLine().charAt(0);
                }
                if(scanner.hasNextLine()){
                    String linemesin = scanner.nextLine();
                    String[] _linemesin = linemesin.split(" ");
                    for(int l=0;l<=_linemesin.length-1;l++){
                        String tempS = _linemesin[l];
                        listtemp.add(Integer.parseInt(tempS));
                    }
                }
                Vendor tempV = new Vendor(nama,listtemp);
                ListVendor.add(tempV);
            }
            Harga = new int[ListMesin.size()];
            for(int j=0;j<ListMesin.size();j++){
                int temp = Integer.parseInt(scanner.next());
                Harga[j] = temp;
                ListMesin.get(j).Harga=temp;
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        try{
            Scanner scanner = new Scanner(new FileReader(namafile));
            int i = 1;
            while(scanner.hasNextLine() && i<=(2*jumlahMesin + 2 + 2*jumlahVendor)){
                scanner.nextLine();
                i++;
            }
            if(scanner.hasNextLine()){
                sizeX = Integer.parseInt(scanner.next());
                sizeY = Integer.parseInt(scanner.next());
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        map = new char[sizeX][sizeY]; //konstruksi dimensi abtraksi map
        try{
            Scanner scanner = new Scanner(new FileReader(namafile));
            int z = 1;
            while(scanner.hasNextLine() && z<=(2*jumlahMesin + 3 + 2*jumlahVendor)){
                scanner.nextLine();
                z++;
            }
            String line;
            while(scanner.hasNextLine()){  
                for(int j=0;j<=sizeY-1;j++){
                    line = scanner.nextLine();
                    for(int i=0;i<=sizeX-1;i++){
                        map[i][j] = line.charAt(i);
                        if(map[i][j] == 'P'){
                            PLocationX = i;
                            PLocationY = j;
                        }
                        else if(map[i][j] != 'P' && map[i][j] != '.' && map[i][j] != '#'){
                            //vendor
                            Coordinate tempCoor = new Coordinate(i,j);
                            for(int a=0;a<=ListVendor.size()-1;a++){
                                if(ListVendor.get(a).getName() == map[i][j]){
                                    ListVendor.get(a).setposition(tempCoor);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    public ArrayList<Vendor> getListVendor(){
        return ListVendor;
    }
    public ArrayList<Machine> getListMachine(){
        return ListMesin;
    }
    public int getTotalMesin(){
        return jumlahMesin;
    }
    public int getModal(){
        return Modal;
    }
    public int getWorkHours(){
        return WorkHours;
    }
    public void setMaxSewaMesin(int id){
        MaxSewaMesin= id;
    }
    public void setModal(int id){
        Modal= id;
    }
    public int getMaxSewaMesin(){
        return MaxSewaMesin;
    }
    public char[][] getMap(){
        return map;
    }
    public Coordinate getPLocation(){
        Coordinate P = new Coordinate(PLocationX,PLocationY);
        return P;
    }
    public int getSizeX(){
        return sizeX;
    }
    public int getSizeY(){
        return sizeY;
    }
    public int getmaxWeight(){
        return maxWeight;
    }
    public int[] getHarga(){
        return Harga;
    }
    public void showMatrix(){
        for(int j=0;j<=sizeY-1;j++){
            for(int i=0;i<=sizeX-1;i++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("sizeX = " + sizeX + ", sizeY = " + sizeY);
        System.out.println("jumlahMesin : " + jumlahMesin);
        System.out.println("maxWeight : " + maxWeight);
        System.out.println("jumlahVendor : " + jumlahVendor);
        System.out.println("listVendor (vendor - koordinat - mesin):");
        for(int z=0;z<=ListVendor.size()-1;z++){
            ListVendor.get(z).printVendor();
        }
    }
}
