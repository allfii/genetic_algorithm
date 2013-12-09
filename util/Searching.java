package util;
import core.MapTranslator;
import java.util.ArrayList;
import java.io.*;
public class Searching {
    //atribut
    private Input input = new Input(new ArrayList<Integer>());;
    private ArrayList<Solution> SolutionList; //tempat penyimpan hasil yang akan dikeluarkan oleh algoritma sesuai mode yg dipilih
    private MapTranslator MapInfo;
    //method
    public Searching(Input inputan,MapTranslator Map){        
        MapInfo = Map;
        SolutionList = new ArrayList<Solution>();
        BFS doBFS = new BFS(inputan,Map);
        doBFS.initSearching();
        doBFS.doSearch();
        SolutionList = new ArrayList<Solution>(doBFS.getSolution());
    }

    public ArrayList<Solution> getSolutionList(){
        return SolutionList;
    }

    public ArrayList<Integer> getShortestTrack(Coordinate X, int mesintujuan){
        int min = 999;
        int idx = 999;
        char ori = ' ';
        ArrayList<Integer> listtemp = new ArrayList<Integer>();
        if(MapInfo.getPLocation().isEqual(X)){
            ori = 'P';
        }
        else{
            for(int i=0;i<=MapInfo.getListVendor().size()-1;i++){
                if(MapInfo.getListVendor().get(i).getposition().isEqual(X)){
                    ori = MapInfo.getListVendor().get(i).getName();
                    break;
                }
            }
        }
        for(int j=0;j<=SolutionList.size()-1;j++){
            if(SolutionList.get(j).getasal() == ori && SolutionList.get(j).getmesin() == mesintujuan
                    && SolutionList.get(j).getlistcoor().size() < min){
                idx = j;
                min = SolutionList.get(j).getlistcoor().size();
            }
        }
        for(int i=0;i<SolutionList.get(idx).getlistjalur().length();i++){
            char c = SolutionList.get(idx).getlistjalur().charAt(i);
            listtemp.add(c-48);
        }
        return listtemp;
    }
    public String getShortestTrackString(Coordinate X, int mesintujuan){
        int min = 999;
        int idx = 999;
        char ori = ' ';
        if(MapInfo.getPLocation().isEqual(X)){
            ori = 'P';
        }
        else{
            for(int i=0;i<=MapInfo.getListVendor().size()-1;i++){
                if(MapInfo.getListVendor().get(i).getposition().isEqual(X)){
                    ori = MapInfo.getListVendor().get(i).getName();
                    break;
                }
            }
        }
        for(int j=0;j<=SolutionList.size()-1;j++){
            if(SolutionList.get(j).getasal() == ori && SolutionList.get(j).getmesin() == mesintujuan
                    && SolutionList.get(j).getlistcoor().size() < min){
                idx = j;
                min = SolutionList.get(j).getlistcoor().size();
            }
        }
        return SolutionList.get(idx).getlistjalur();
    }
    
    public ArrayList<Coordinate> getShortestPath(Coordinate X, int mesintujuan){
        ArrayList<Coordinate> retval = new ArrayList<Coordinate>();
        int min = 999;
        int idx = 999;
        char ori = ' ';
        if(MapInfo.getPLocation().isEqual(X)){
            ori = 'P';
        }
        else{
            for(int i=0;i<=MapInfo.getListVendor().size()-1;i++){
                if(MapInfo.getListVendor().get(i).getposition().isEqual(X)){
                    ori = MapInfo.getListVendor().get(i).getName();
                    break;
                }
            }
        }
        for(int j=0;j<=SolutionList.size()-1;j++){
            if(SolutionList.get(j).getasal() == ori && SolutionList.get(j).getmesin() == mesintujuan
                    && SolutionList.get(j).getlistcoor().size() < min){
                idx = j;
                min = SolutionList.get(j).getlistcoor().size();
            }
        }
        ArrayList<Coordinate> result = new ArrayList<Coordinate>(SolutionList.get(idx).getlistcoor());
        return result;
    }
}
