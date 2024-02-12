import java.util.Random;

public class Depositor implements Runnable {
    //Class variables assigned from constructor
    private String threadName;
    private TheBank account;

    //Creating random number generator object
    private static Random generator = new Random();
    private int randomDeposit;
    private int randomSleep;
    
    //Largest deposit allowed 
    private static final int MAX_DEPOSIT = 500;
    private static final int MAXSLEEPING = 1500;

    //Constructor
    public Depositor(TheBank accountArg, String nameArg){
        this.threadName = nameArg;
        this.account = accountArg;
    }


    @Override
    public void run(){
        //Make it run infinitely
        while(true){
            try{
                //Create the random value and try to put it in bacnk
                randomDeposit = generator.nextInt(MAX_DEPOSIT) + 1;
                account.deposit(randomDeposit, threadName);
                //Put thread to sleep
                randomSleep = generator.nextInt(MAXSLEEPING) + 1;
                Thread.sleep(randomSleep);
            }catch(InterruptedException exception){
                System.out.println("Error Depositing thread");
            }
        }
    }
}
