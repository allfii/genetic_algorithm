package core;
import util.Coordinate;
import java.util.ArrayList;

public class Vendor {
    private char Name;
    private ArrayList<Integer> listMesin;
    private Coordinate position;
    
    public Vendor(char nama, ArrayList<Integer> mesin){
        Name = nama;
        listMesin = new ArrayList<Integer>(mesin);
    }
    public Vendor(Vendor X){
        Name = X.getName();
        listMesin = new ArrayList<Integer>(X.getlistMesin());
        position = new Coordinate(X.getposition());
    }
    public char getName(){
        return Name;
    }
    public ArrayList<Integer> getlistMesin(){
        return listMesin;
    }
    public Coordinate getposition(){
        return position;
    }
    public void setposition(Coordinate X){
        position = new Coordinate(X);
    }
    public boolean isAvailable(int M){
        return listMesin.contains(M);
    }
    public void printVendor(){
        System.out.print(Name + " - ");
        position.showCoordinat();
        System.out.print(" - ");
        for(int i=0;i<=listMesin.size()-1;i++){
            System.out.print(listMesin.get(i) + " ");
        }
        System.out.println();
    }
}
