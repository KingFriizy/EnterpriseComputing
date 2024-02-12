import java.io.IOException;
import java.util.concurrent.locks.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.FileWriter;

public class TheBank implements TheBankInterface{
    //Variables

    //Defaults to a non-fair lock that gives any thread a chance to get the next lock acces regardless if they were wating longer or not 
    private Lock accessLock = new ReentrantLock();
    private Condition canWithdraw = accessLock.newCondition(); //Condition to prevent the same withdraw thread from tring to withdraw when it clearly cant
    private Condition canDeposit = accessLock.newCondition(); //Condtion to prevent deposits if audit is working on output

    private int balance = 0;
    private int transactionNum = 0;
    private int sinceInternal = 0;
    private int sinceTreasury = 0;
    private int treasuryCounter = 0;
    private int internalCounter = 0;

    //Using auditBusy variable to pause deposits/withdraws when audit is in effect
    private boolean auditBusy = false;

    private final int DEPOSIT_ALERT = 350;
    private final int WITHDRAW_ALERT = 75;



    public void deposit(int depositAmount, String threadName){
        //lock the bank account
        accessLock.lock();
        //wait for the auditor to complete
        try{
            //Check if the deposit should be flagged
            if(depositAmount >= DEPOSIT_ALERT){
                flaggedTransaction(depositAmount, threadName, "Deposit");
            }
            
            //Nothing will prevent a deposit. Deposits will still happen if flagged
            balance += depositAmount;
            transactionNum++;
            internalCounter++;
            treasuryCounter++;

            //Display the balance
            System.out.print(threadName+" deposits $"+depositAmount+"\t\t\t\t");
            System.out.println("\t\t\t\t\t\t\t(+) Balance is $"+balance+"\t\t\t\t\t\t\t\t"+ transactionNum);
            
            //Tell the withdraw threads to try withdrawing again
            canWithdraw.signalAll();
        }catch(Exception exception){
            System.out.println("Error in the Bank deposit function");
        }finally{
            //unlock the bank account so another thread can use
            accessLock.unlock();
        }

    }

    public void withdraw(int withdrawAmount,String threadName){
        //lock the bank account
        accessLock.lock();

        //wait for  auditor to complete (ignoring this for now)

        //output thread information, withdrawal amount, and account balance. Block on insufficient funds

        try{
            //check balance
            //if balance too low
            if((balance - withdrawAmount) < 0){
                System.out.println("\t\t (******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS");
                flaggedTransaction(withdrawAmount, threadName, "Withdraw");
                canWithdraw.await();
            }
                //block for deposit to occur
            //else
            else{
                if(withdrawAmount >= WITHDRAW_ALERT){
                    flaggedTransaction(withdrawAmount, threadName, "Withdraw");
                }
                //make withdrawal
                balance -= withdrawAmount;
                transactionNum++;
                internalCounter++;
                treasuryCounter++;
                //hanlde transaction log for flagged transaction

                //Checking if withdraw amount is too high
            //Display the balance


                System.out.print(threadName+" withdraws $"+withdrawAmount+"\t\t\t\t");
                System.out.println("\t\t\t\t\t\t\t(-) Balance is $"+balance+"\t\t\t\t\t\t\t\t"+ transactionNum);

            }
               
        }catch(Exception e){
            System.out.println("Error with withdrawal");
        }finally{
            //unlock the bank account
            accessLock.unlock();
        }
    }

    public void internalAudit(){
        //lock the bank acocunt
        accessLock.lock();
        //wait for auditor to complete
        //output thread information , account balance

        try{
            //run audit
            sinceInternal = internalCounter;
            System.out.println("*******************************************************************************************************************\n");
            System.out.println("INTERNAL BANK AUDITOR FINDS CURRENT ACCOUNT BALANCE TO BE: $" + balance + "\t\t NUMBER OF TRANSACTIONS SINCE LAST AUDIT: " + sinceInternal);
            System.out.println("\n*****************************************************************************************************************");
    
            //reset the counter
            internalCounter = 0;
            //signal all waiting for audit to complete
        }catch(Exception e){
            System.out.println("An exception was thrown getting the balance by the auditor");
        }finally{
            //unlock the bank account
            accessLock.unlock();
        }
    }

    public void treasuryAudit(){
        //Same thing as the internal audit
        accessLock.lock();

        try{
            //run audit
            sinceTreasury = treasuryCounter;
            System.out.println("*******************************************************************************************************************\n");
            System.out.println("TREASURY AUDITOR FINDS CURRENT ACCOUNT BALANCE TO BE: $" + balance + "\t\t NUMBER OF TRANSACTIONS SINCE LAST AUDIT: " + sinceTreasury);
            System.out.println("\n*****************************************************************************************************************");
    
            //reset the counter
            treasuryCounter = 0;
            //signal all waiting for audit to complete
        }catch(Exception e){
            System.out.println("An exception was thrown getting the balance by the treasury audit");
        }finally{
            //unlock the bank account
            accessLock.unlock();
        }
    }

    

    public void flaggedTransaction(int flaggedAmount, String threadName, String type){
        if(type == "Withdraw"){
            System.out.println("\n * * * Flagged Transaction - Withdrawal Agent " + threadName + " Made A Withdrawal In Excess of $" + WITHDRAW_ALERT + ".00 USD - See Flagged Transaction Log \n");
        }
        else{
            System.out.println("\n * * * Flagged Transaction - Deposit Agent " + threadName + " Made A Deposit In Excess of $" + DEPOSIT_ALERT + ".00 USD - See Flagged Transaction Log \n");
        }



        /*Logging the Transaction below*/

        //Generate date object or timestamp
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        
        //set date object format
        //craete and output transition file
        FileWriter transactionFile= null;

        try{
            //set file pointer
            
            //set up a printWriter object
            //create the timestamp

            transactionFile = new FileWriter("transactions.csv", true);
            transactionFile.append("    ");
            transactionFile.append(type + "Agent"+threadName+" issued " + type + " of "+flaggedAmount +".00 at:");
            transactionFile.append(date.toString() + " ");
            transactionFile.append(time.toString());
            transactionFile.append("\n");
               
        }catch(IOException e){
            System.out.println("\nError wrtiting into transaction file");
        }finally{
            //close file writer
            try{
                transactionFile.close();
            }catch(IOException e){
                System.out.println("\nError closing transaction File");
            }
        }
        
    }
}