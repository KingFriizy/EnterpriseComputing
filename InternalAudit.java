import java.util.Random;

public class InternalAudit implements Runnable{
    private static Random generator = new Random();
    private TheBank account;

    private int randomSleep;
    public InternalAudit(TheBank accountArg){
        this.account = accountArg;
    }

    @Override
    public void run(){
        while(true){
            try{
                account.internalAudit();
                randomSleep = generator.nextInt(1500);

                Thread.sleep(randomSleep);
            }catch(Exception e){
                System.out.println("Error in Internal audit thread class");

            }
        }
    }
    
}
