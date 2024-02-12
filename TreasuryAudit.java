import java.util.Random;
public class TreasuryAudit implements Runnable {
    private static Random generator = new Random();
    private TheBank account;

    private int randomSleep;
    public TreasuryAudit(TheBank accountArg){
        this.account = accountArg;
    }

    @Override
    public void run(){
        //run infinitely
        while(true){
            try{
                account.treasuryAudit();
                randomSleep = generator.nextInt(1500) + 2000;

                Thread.sleep(randomSleep);
            }catch(Exception e){
                System.out.println("Error in Treasury audit thread class");
            }
        }
    }
    
}
