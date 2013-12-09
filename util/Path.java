package util;

import java.util.ArrayList;

public class Path {
    private ArrayList<Coordinate> Path;
    public Path(ArrayList<Coordinate> path){
        Path = path;
    }
    public Path(){
        Path = new ArrayList<Coordinate>();
    }
    public ArrayList<Coordinate> getPath(){
        return Path;
    }
    
}
