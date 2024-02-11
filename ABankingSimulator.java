/*Name: Richard Wright
 * Course: CNT 4714 Spring 2024
 * Assignment Title: Project 2 - Synchronized, Cooperating Threads Under Locking
 * Due Date: February 11, 2024
 */


//Driver File

import java.util.concurrent.*;
public class ABankingSimulator{
    //Max number of threads in thread pool
    public static final int MAX_AGENTS = 17;

    public static void main(String[] args) {
        ExecutorService application = Executors.newFixedThreadPool(MAX_AGENTS);


        //Creating account object
        TheBank account  = new TheBank();

        try{
            System.out.println("* * *   SIMULATION BEGINS...");
            System.out.println();
            System.out.println("Deposit Agents\t\t\t   Withdrawal Agents\t\t\t   Balance");		
            System.out.println("--------------\t\t\t   -----------------\t\t\t   -------");

            

            //Creating 5 depositor threads
            Depositor dt1 = new Depositor(account, "Agent DT1");
            Depositor dt2 = new Depositor(account, "Agent DT2");
            Depositor dt3 = new Depositor(account, "Agent DT3");
            Depositor dt4 = new Depositor(account, "Agent DT4");
            Depositor dt5 = new Depositor(account, "Agent DT5");
            

            //Creating 10 withdrawal Threads
            Withdrawal wt1 = new Withdrawal(account, "Agent WT1");
            Withdrawal wt2 = new Withdrawal(account, "Agent WT2");
            Withdrawal wt3 = new Withdrawal(account, "Agent WT3");
            Withdrawal wt4 = new Withdrawal(account, "Agent WT4");
            Withdrawal wt5 = new Withdrawal(account, "Agent WT5");
            Withdrawal wt6 = new Withdrawal(account, "Agent WT6");
            Withdrawal wt7 = new Withdrawal(account, "Agent WT7");
            Withdrawal wt8 = new Withdrawal(account, "Agent WT8");
            Withdrawal wt9 = new Withdrawal(account, "Agent WT9");
            Withdrawal wt10 = new Withdrawal(account, "Agent WT10");

            //Creating 2 Auditor threads
            InternalAudit internalAudit = new InternalAudit();
            TreasuryAudit treasuryAudit = new TreasuryAudit();

            //Execute all threads
            application.execute(dt1);
            application.execute(dt2);
            application.execute(dt3);
            application.execute(dt4);
            application.execute(dt5);

            application.execute(wt1);
            application.execute(wt2);
            application.execute(wt3);
            application.execute(wt4);
            application.execute(wt5);
            application.execute(wt6);
            application.execute(wt7);
            application.execute(wt8);
            application.execute(wt9);
            application.execute(wt10);

            application.execute(internalAudit);
            application.execute(treasuryAudit);
        }catch(Exception exception){
            exception.printStackTrace();
        }

        application.shutdown();
    }
}