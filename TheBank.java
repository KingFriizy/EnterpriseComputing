import java.io.IOException;
import java.util.concurrent.locks.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;

public class TheBank implements TheBankInterface{

    //Variables
    private Lock accessLock = new ReentrantLock(); //Defaults to a non-fair lock
    private Condition canWithdraw = accessLock.newCondition(); //Condition to prevent the same withdraw thread from tring to withdraw when it clearly cant
    private int balance = 0;
    private int transactionNum = 0;
    private int sinceInternal = 0;
    private int sinceTreasury = 0;
    private int treasuryCounter = 0;
    private int internalCounter = 0;

    private final int DEPOSIT_ALERT = 350;
    private final int WITHDRAW_ALERT = 75;



    public void deposit(int depositAmount, String threadName){
        //Lock the bank account
        accessLock.lock();
        
        try{
            //Always run deposits
            balance += depositAmount;
            transactionNum++;
            internalCounter++;
            treasuryCounter++;

            //Display the balance
            System.out.print(threadName+" deposits $"+depositAmount+"\t\t\t\t");
            System.out.println("\t\t\t(+) Balance is $"+balance+"\t\t\t\t\t"+ transactionNum);

            //Check if the deposit should be flagged
            if(depositAmount >= DEPOSIT_ALERT){
                flaggedTransaction(depositAmount, threadName, "Deposit");
            }
            
            //Tell the withdraw threads to try withdrawing again
            canWithdraw.signalAll();
        }catch(Exception exception){
            System.out.println("Error in the Bank deposit function");
        }finally{
            //Unlock the bank account
            accessLock.unlock();
        }

    }

    public void withdraw(int withdrawAmount,String threadName){
        //Lock the bank account
        accessLock.lock();
        try{
            //If balance too low, send to flagged method
            if((balance - withdrawAmount) < 0){
                //System.out.print("\t\t\t\t" + threadName + " withdraws $"+ withdrawAmount);
                //System.out.println("\t\t\t(******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS");

                System.out.println("\t\t\t\t" + threadName + " withdraws $"+ withdrawAmount+"\t\t\t(******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS");
                flaggedTransaction(withdrawAmount, threadName, "Withdraw");
                canWithdraw.await();
            }
                
            //Balance is fine, continue processing withdrawal
            else{
                //Make withdrawal
                balance -= withdrawAmount;
                transactionNum++;
                internalCounter++;
                treasuryCounter++;
                
                //Displaying Balance in terminal
                System.out.println("\t\t\t\t" + threadName+" withdraws $"+withdrawAmount+"\t\t\t(-) Balance is $"+balance+"\t\t\t\t\t"+ transactionNum);

                //Check if withdrawal should be flagged
                if(withdrawAmount >= WITHDRAW_ALERT){
                    flaggedTransaction(withdrawAmount, threadName, "Withdraw");
                }
            }
               
        }catch(Exception e){
            System.out.println("Error with withdrawal");
        }finally{
            //Unlock after processing withdrawal
            accessLock.unlock();
        }
    }

    public void internalAudit(){
        //Lock bank until audit process
        accessLock.lock();

        //Run audit
        try{
            //Set 'since' variable to the counter (counter has the number of transactions saved since last audit)
            sinceInternal = internalCounter;
            System.out.println("*******************************************************************************************************************\n");
            System.out.println("INTERNAL BANK AUDITOR FINDS CURRENT ACCOUNT BALANCE TO BE: $" + balance + "\t\t NUMBER OF TRANSACTIONS SINCE LAST AUDIT: " + sinceInternal);
            System.out.println("\n*****************************************************************************************************************");
    
            //Reset audit counter
            internalCounter = 0;
        }catch(Exception e){
            System.out.println("An exception was thrown getting the balance by the auditor");
        }finally{
            //Unlock the bank account
            accessLock.unlock();
        }
    }

    public void treasuryAudit(){
        //Same thing as the internal audit
        accessLock.lock();

        try{
            sinceTreasury = treasuryCounter;
            System.out.println("*******************************************************************************************************************\n");
            System.out.println("TREASURY AUDITOR FINDS CURRENT ACCOUNT BALANCE TO BE: $" + balance + "\t\t NUMBER OF TRANSACTIONS SINCE LAST AUDIT: " + sinceTreasury);
            System.out.println("\n*****************************************************************************************************************");
    
            treasuryCounter = 0;

        }catch(Exception e){
            System.out.println("An exception was thrown getting the balance by the treasury audit");
        }finally{
            //Unlock the bank account
            accessLock.unlock();
        }
    }

    

    public void flaggedTransaction(int flaggedAmount, String threadName, String type){
        /*For type 'withdraw': 
         * Case 1: Withdraw amount causes alert-> Flag transaction in terminal and process into transaction file
         * Case 2: Insuffient funds-> Simply return (Insufficient funds don't get logged in transaction file)
         */
        if(type == "Withdraw"){
            if(flaggedAmount >= WITHDRAW_ALERT){
                System.out.println("\n * * * Flagged Transaction - Withdrawal Agent " + threadName + " Made A Withdrawal In Excess of $" + WITHDRAW_ALERT + ".00 USD - See Flagged Transaction Log \n");
            }
            //Insufficient fund transactions dont get logged in the transaction file
            else{
                return;
            }
        //If type is deposit
        }
        else{
            System.out.println("\n * * * Flagged Transaction - Deposit Agent " + threadName + " Made A Deposit In Excess of $" + DEPOSIT_ALERT + ".00 USD - See Flagged Transaction Log \n");
        }

        //Generate date object or timestamp
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = time.format(formatter2);


        
        //set date object format
        //Create and output transaction file
        FileWriter transactionFile = null;

        try{
            transactionFile = new FileWriter("transactions.csv", true);
            if(type == "Withdraw"){
                transactionFile.append("\t");
            }
            transactionFile.append(type + " Agent issued " + type + " of $"+flaggedAmount +".00 at: ");
            transactionFile.append(formattedDate + " ");
            transactionFile.append(formattedTime + " EST");
            transactionFile.append(" Transaction Number: "+ transactionNum);
            transactionFile.append("\n");
               
        }catch(IOException e){
            System.out.println("\nError wrtiting into transaction file");
        }finally{
            //Close file writer
            try{
                transactionFile.close();
            }catch(IOException e){
                System.out.println("\nError closing transaction File");
            }
        }
        
    }
}