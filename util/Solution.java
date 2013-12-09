package util;
import util.Coordinate;
import java.util.ArrayList;

public class Solution {
    private int mesin;
    private char asal;
    private char vendor;
    private ArrayList<Coordinate> listcoor;
    private String listjalur;
    
    public Solution(int x, char a, char v, ArrayList<Coordinate> y, String z){
        asal = a;
        mesin = x;
        vendor = v;
        listcoor = new ArrayList<Coordinate>(y);
        listjalur = z;
    }
    public int getmesin(){
        return mesin;
    }
    public char getasal(){
        return asal;
    }
    public char getvendor(){
        return vendor;
    }
    public ArrayList<Coordinate> getlistcoor(){
        return listcoor;
    }
    public String getlistjalur(){
        return listjalur;
    }
}
