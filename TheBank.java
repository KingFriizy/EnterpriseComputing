import java.io.IOException;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.ReentrantLock;

public class TheBank implements TheBankInterface{
    //Variables
    private Lock accessLock = new ReentrantLock();
    private Condition canWithdraw = accessLock.newCondition();
    private Condition canDeposit = accessLock.newCondition();

    private int balance = 0;
    private int transactionNum = 0;
    private int sinceInternal = 0;
    private int sinceTreasury = 0;
    private int treasuryCounter = 0;
    private int internalCounter = 0;

    boolean occupied = false;

    private final int DEPOSIT_ALERT = 350;
    private final int WITHDRAW_ALERT = 75;


    public void flaggedTransaction(){
        //Generate date object or timestamp
        //set date object format
        //crate a stringbuilder object
        //craete and output transition file

        try{
            //set file pointer
            //set up a printWriter object
            //create the timestamp

            if//if statement for the deposite type of transaction
            else//else statement for withdraw type of transaction

            //write the string into the transaction file
        }catch(IOException e){
            System.out.println("\nError wrtiting into transaction file");
        }finally{
            //close file writer
        }
        
    }

    public void deposit(){
        //lock the bank account
        //wait for the auditor to complete
        //output the thread information, deposit amount and account balance3, then signal any waiting withdrawal threads

        try{
            //make deposit
            //handle transaction logging for flagged transaction

            //signal all waiting withdrawal threads waiting to make a withdrawal
        }catch(Exception e2){
            System.out.println("Exception thrown depositing");
        }finally{
            //unlock the bank account
            
        }
    }

    public void withdraw(){
        //lock the bank account

        //wait for  auditor to complete

        //output thread information, withdrawal amount, and account balance. Block on insufficient funds

        try{
            //check balance
            //if balance too low
                //block for deposit to occur
            //else
                //make withdrawal
                //hanlde transaction loggin for flagged transaction
            
        }catch(Exception e){
            System.out.println("Error with withdrawal");
        }finally{
            //unlock the bank account
        }
    }

    public void internalAudit(){
        //lock the bank acocunt
        //wait for auditor to complete
        //output thread information , account balance

        try{
            //run audit
            //signal all waiting for audit to complete
        }catch(Exception e){
            System.out.println("An exception was thrown getting the balance by the auditor");
        }finally{
            //unlock the bank account
        }
    }

    public void treasuryAudit(){
        //Same thing as the internal audit
    }
}