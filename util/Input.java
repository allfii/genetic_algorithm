package util;
import java.util.ArrayList;

public class Input {
    //atribut
    private ArrayList<Integer> listbarang; //daftar barang yang dibutuhkan pabrik
    public Input(ArrayList<Integer> urutanbarang){
        listbarang = new ArrayList<Integer>(urutanbarang);
    }
    public ArrayList<Integer> getlistbarang(){
        return listbarang;
    }
    public void setlistbarang(ArrayList<Integer> listinput){
        listbarang = new ArrayList<Integer>(listinput);
    }
}
