/*
 *****************************
  Компьютерный практикум №4
  ФИ-52, Ершов С., Овчарова М.
 *****************************
 */
package jiffygen;

import java.util.*;
import java.io.*;

class LSR {
    private final int period;
    private final int[] condition;
    private final int[] recurrence;
    
    LSR(int period) {
        this.period = period;
        condition = new int[period];
        recurrence = new int[period];
    }
    
    void setCondition(int[] arr){
        for(int i=0;i<period;i++)
            condition[i] = arr[i];
    }
    
    void setRecurrence(int[] arr){
        for(int i=0;i<period;i++)
            recurrence[i] = arr[i];
    }
    
    int getPeriod(){
        return period;
    }
    
    int tact(){
        int res = condition[0];
        int el=0;
        for(int i=0;i<period;i++)
            el += condition[i]*recurrence[i];
        el = el % 2;
        for(int i=0;i<period-1;i++)
            condition[i] = condition[i+1];
        condition[period-1] = el;
        return res;
    }
}

public class JiffyGen {
    final static int F_PERIOD = 30; //период первого регистра
    final static int[] F_RECUR = {1,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //рекурента первого регистра
    final static int S_PERIOD = 31;
    final static int[] S_RECUR = {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    final static int T_PERIOD = 32;
    final static int[] T_RECUR = {1,1,1,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    final static double FALSE_REJECTION = 0.01;
    final static String PATH_TO_Z = "D:\\Programming\\Crypt\\JiffyGen\\text\\z.txt";
    final static String PATH_L1 = "D:\\Programming\\Crypt\\JiffyGen\\text\\l1.txt";
    
    private LSR l1;
    private LSR l2;
    private LSR l3;
    
    JiffyGen(int F_PERIOD, int[] F_RECUR, int S_PERIOD, int[] S_RECUR, int T_PERIOD, int[] T_RECUR){
        l1 = new LSR(F_PERIOD);
        l1.setRecurrence(F_RECUR);
        l2 = new LSR(S_PERIOD);
        l2.setRecurrence(S_RECUR);
        l3 = new LSR(T_PERIOD);
        l3.setRecurrence(T_RECUR);
    }
    
    void setConditionToL(int number, int[] cond){
        switch (number){
                case 1:
                    l1.setCondition(cond);
                    break;
                case 2:
                    l2.setCondition(cond);
                    break;
                case 3:
                    l3.setCondition(cond);
                    break;
                default:
                    System.out.println("Wrong number!!!");
                    break;
        }
    }
    
    int tact(){
        int res;
        int s = l3.tact();
        if (s==1){
            res = l1.tact();
            l2.tact();
        }else{
            l1.tact();
            res = l2.tact();
        }
        return res;   
    }
    
    int tact(int flag){
        int res;
        int s = flag;
        if (s==1){
            res = l1.tact();
        }else{
            res = l2.tact();
        }
        return res; 
    }
    
    public static long pow(int base,int degree){
        if (degree!=1)
            return base*pow(base,--degree);
        else
            return base;
    }
    
    public static ArrayList<Integer> readFile(String path) throws IOException{
        ArrayList<Integer> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path),"CP1251"));
        String s;
        while((s=br.readLine()) != null){
            sb = sb.append(s);
        } 
        String text = sb.toString();
        int border = text.length();
        for (int i =0;i<border;i++)
            list.add((int)text.charAt(i)-48);
        return list;
    }
    
    public static void main(String[] args) throws IOException {
        final int Nl1 = 66;
        final int Cl1 = 25;
        JiffyGen jg = new JiffyGen(F_PERIOD, F_RECUR, S_PERIOD, S_RECUR, T_PERIOD, T_RECUR);
    
        ArrayList<Integer> z = new ArrayList<>();
        try{
            z = readFile(PATH_TO_Z);
        }
        catch(IOException ex){
            System.out.print(ex.getMessage());
        }
        
        
        
        try {
            FileWriter fw=new FileWriter(PATH_L1);
        
        int[] vect = new int[F_PERIOD];
        int[] out = new int[Nl1];
        int buf=0;
        int R =0;
        long border = pow(2,F_PERIOD);
        System.out.println(border);
        for(int i=0;i<border;i++){
            R=0;
            buf = i;
            for(int j=0;j<F_PERIOD;j++){
                vect[j]=buf&0b1;
                buf = buf >> 1;
            }
            jg.setConditionToL(1, vect);
            for(int j=0;j<Nl1;j++)
                out[j] = jg.tact(1);
            for(int j=0;j<Nl1;j++)
                R+=(out[j]+z.get(j))%2;
            // System.out.println(R);
            if (R<=Cl1){
                for(int k=0;k<F_PERIOD;k++)
                    fw.append(Integer.toString(vect[k]));
                fw.append("\r\n");
                fw.flush();
            }
        }
        fw.close();
        }
        catch (Exception ex) {
            System.out.println(ex.toString()); //чтобы хоть что-то знать о возможной ошибке
        }
        
        
        
        
      
        
    }
    
}
