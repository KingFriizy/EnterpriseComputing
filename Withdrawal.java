import java.util.Random;
public class Withdrawal implements Runnable {
    //Class variables assigned from constructor
    private String threadName;
    private TheBank account;
    
    //Largest deposit allowed 
    private static final int MAX_WITHDRAW = 99;
    private static final int MAXSLEEPING = 500;

    //Random variables
    private int randomWithdraw;
    private int randomSleep;
    private static Random generator =  new Random();

    //Constructor
    public Withdrawal(TheBank accountArg, String name){
        this.threadName = name;
        this.account = accountArg;
    }


    @Override
    public void run(){
        //Run the thread inifinitely
        while(true){
            try{
                //Generate random withdraw amount
                randomWithdraw = generator.nextInt(MAX_WITHDRAW);
                randomSleep = generator.nextInt(MAXSLEEPING);
                
                account.withdraw(randomWithdraw, threadName);
                Thread.sleep(randomSleep);
            }catch(Exception exception){
                System.out.println("Error in the withdraw thread class");
            }
        }

    }

    
}
