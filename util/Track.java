package util;

public class Track {
    //atribut
    private String Jalur;
    public Track(){
    }
    public Track(String _Jalur){
        Jalur = _Jalur;
    }
    public String getJalur(){
        return Jalur;
    }
    public Track addJalur(int x){
        String temp = Jalur + x;
        Track _temp = new Track(temp);
        return _temp;
    }
    public Track mergeJalur(String X){
        String temp = Jalur + X;
        Track _temp = new Track(temp);
        return _temp;
    }
    public void removelastJalur(){
        if(Jalur.length() == 1){
            Jalur = new String();
        }
        else if(Jalur.length() > 1){
            Jalur = Jalur.substring(0, Jalur.length()-2);
        }
        else{
            //do nothing
        }
    }
    public void showTrack(){
        if(Jalur.isEmpty()){
            System.out.println("No Solution");
        }
        else if(Jalur.length() == 1){
            System.out.println(Jalur.charAt(0));
        }
        else{
            System.out.print(Jalur.charAt(0));
            for(int i=1;i<=Jalur.length()-1;i++){
                System.out.print("->" + Jalur.charAt(i));
            }
            System.out.println();
        }
    }
}
