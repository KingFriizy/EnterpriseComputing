public interface TheBankInterface {
    public abstract void deposit(int depositAmount, String threadName);

    public abstract void withdraw(int withdrawAmount, String threadName);

    public abstract void flaggedTransaction(int flaggedAmount, String threadName, String type);

    public abstract void internalAudit();

    public abstract void treasuryAudit();
    
}
