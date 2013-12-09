package util;

public class Coordinate {
    public int x;
    public int y;
    //constructor
    public Coordinate(int _x, int _y){
        x = _x;
        y = _y;
    }
    //copy
    public Coordinate(Coordinate X){
        x = X.x;
        y = X.y;
    }
    public boolean isEqual(Coordinate X){
        if(X.x == this.x && X.y == this.y){
            return true;
        }
        else{
            return false;
        }
    }
    public void showCoordinat(){
        System.out.print("(" + x + "," + y +")");
    }
    public Coordinate move(int i){
        Coordinate temp = new Coordinate(this);
        //0 = bawah
        if(i == 0){
            temp.y = y+1;
        }
        //1 = kiri
        else if(i == 1){
            temp.x = x-1;
        }
        //2 = atas
        else if(i == 2){
            temp.y = y-1;
        }
        //3 = kanan
        else if(i == 3){
            temp.x = x+1;
        }
        else{
            //do nothing
        }
        return temp;
    }
}
