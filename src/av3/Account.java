package av3;

public abstract class Account {

    private String accountOwner;
    private int id;
    private static int idSeed = 10000;
    private double currentAmount;
    private AccountType accountType;

    public Account(String accountOwner, int id, double currentAmount) {
        this.accountOwner = accountOwner;
        this.currentAmount = currentAmount;
        this.id = ++idSeed;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void addAmout(double amount){
        this.currentAmount+=amount;
    }

    public void withdrawAmount(double amount) throws CannotWithdrawMoneyException {
        if(currentAmount < amount){
            throw new CannotWithdrawMoneyException(currentAmount,amount);

        }
        this.currentAmount-=amount;
    }

    public abstract AccountType getAccountType();


    @Override
    public String toString() {
        return String.format("%d: %.2f", id,currentAmount);
    }
}
