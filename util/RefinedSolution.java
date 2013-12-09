package util;
import java.util.ArrayList;
import util.Coordinate;

public class RefinedSolution {
    private int mesinasal;
    private int mesintujuan;
    private ArrayList<Coordinate> listcoor;
    public RefinedSolution(int x, int y, ArrayList<Coordinate> z){
        mesinasal = x;
        mesintujuan = y;
        listcoor = new ArrayList<Coordinate>(z);
    }
    public int getmesinasal(){
        return mesinasal;
    }
    public int getmesintujuan(){
        return mesintujuan;
    }
    public ArrayList<Coordinate> getlistcoor(){
        return listcoor;
    }
}
