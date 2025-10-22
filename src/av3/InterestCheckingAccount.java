package av3;

public class InterestCheckingAccount extends Account implements InterestBearingAccount{

    public static final double INTEREST_RATE = 0.03;
    public InterestCheckingAccount(String accountOwner, int id, double currentAmount) {
        super(accountOwner, id, currentAmount);
    }

    @Override
    public AccountType getAccountType() {
        return getAccountType().INTEREST;
    }

    @Override
    public void addInterest() {
        addAmout(getCurrentAmount() * INTEREST_RATE);
    }
}
