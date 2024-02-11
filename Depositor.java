import java.util.Random;

public class Depositor implements Runnable {
    //Class variables assigned from constructor
    private String threadName;
    private TheBank account;
    
    //Largest deposit allowed 
    private static final int MAX_DEPOSIT = 500;
    private static final int MAXSLEEPING = 1500;

    //Constructor
    public Depositor(TheBank accountArg, String name){
        this.threadName = name;
        this.account = accountArg;
    }


    @Override
    public void run(){

    }
}
