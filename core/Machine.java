package core;
public class Machine {
    private int BebanKerjaPerHari; // T
    private int BebanKerjaPerPeriode; // W
    private int Istirahat; // S
    private int Produktivitas; // P
    private int CostOverload; // C
    private int BiayaSewa; // R
    private int Dependency; // D
    private int AmountDependency; // I
    public int WaktuTempuh;
    public int Harga;
    public Machine(){}
    public Machine(int T, int W, int S, int P,int I, int C, int R, int D){
        BebanKerjaPerHari = T;
        BebanKerjaPerPeriode = W;
        Produktivitas = P;
        Istirahat = S;
        CostOverload = C;
        BiayaSewa = R;
        AmountDependency = I;
        Dependency = D;
    }  
    public int getAmountDependency(){
        return AmountDependency;
    }
    public int getDependency(){
        return Dependency;
    }
    public int getBebanKerjaPerHari(){
        return BebanKerjaPerHari;
    }
    public int getBebanKerjaPerPeriode(){
        return BebanKerjaPerPeriode;
    }
    public int getIstirahat(){
        return Istirahat;
    }
    public int getProduktivitas(){
        return Produktivitas;
    }
    public int getCostOverload(){
        return CostOverload;
    }
    public int getBiayaSewa(){
        return BiayaSewa;
    }
    
    
    public void setAmountDependency(int input){
        AmountDependency = input;
    }
    public void setDependency(int input){
        Dependency = input;
    }
    public void setBebanKerjaPerHari(int input){
        BebanKerjaPerHari = input;
    }
    public void setBebanKerjaPerPeriode(int input){
        BebanKerjaPerPeriode = input;
    }
    public void setIstirahat(int input){
        Istirahat = input;
    }
    public void setProduktivitas(int input){
        Produktivitas = input;
    }
    public void setCostOverload(int input){
        CostOverload = input;
    }
    public void setBiayaSewa(int input){
        BiayaSewa = input;
    }
}
