package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class Log {
    private Writer write;
    
    public Log(){
        write = null;
    }
	
    public void writeLog(ArrayList<String> inisial, ArrayList<String> seleksi, ArrayList<String> crossOver, ArrayList<String> mutasi, String fileName) throws IOException{
        write = new BufferedWriter(new FileWriter(new File(fileName)));
        
        write.write("+--------------------+--------------------+--------------------+--------------------+"+'\n');
        write.write("|   INISIALISASI     |      SELEKSI       |     CROSS OVER     |      MUTASI        |"+'\n');
        write.write("+--------------------+--------------------+--------------------+--------------------+"+'\n');
            for(int i = 0; i < inisial.size(); i++){
                    write.write("| "+inisial.get(i)+" | ");
               if(i >= seleksi.size()){
                   write.write("                   | ");
               }else{
                    write.write(seleksi.get(i)+" | ");
               }
               if(i >= crossOver.size()){
                   write.write("                   | ");
               }else{
                   write.write(crossOver.get(i)+" | ");
               }
               if(i >= mutasi.size()){
                   write.write("                   | "+'\n');
               }else{
                   write.write(mutasi.get(i)+" | "+'\n');
               }
            }
        write.write("+--------------------+--------------------+--------------------+--------------------+"+'\n');
        write.close();
    }
    
    public static void main(String[] args) throws IOException{
        Log log = new Log();
        ArrayList<String> inisial = new ArrayList();
        ArrayList<String> seleksi = new ArrayList();
        ArrayList<String> crossOver = new ArrayList();
        ArrayList<String> mutasi = new ArrayList();
        
        inisial.add("gene1");
        inisial.add("gene2");
        inisial.add("gene3");
        inisial.add("gene4");
        inisial.add("gene5");
        
        seleksi.add("gene6");
        seleksi.add("gene7");
        seleksi.add("gene8");
        seleksi.add("gene9");
        
        crossOver.add("gene10");
        crossOver.add("gene11");
        crossOver.add("gene12");
        crossOver.add("gene13");
        
        mutasi.add("gene14");
        mutasi.add("gene15");
        mutasi.add("gene16");
        mutasi.add("gene17");
        
        log.writeLog(inisial, seleksi, crossOver, mutasi, "log.txt");
    }
}

