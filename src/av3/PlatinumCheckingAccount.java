package av3;

public class PlatinumCheckingAccount extends InterestCheckingAccount{

    public PlatinumCheckingAccount(String accountOwner, int id, double currentAmount) {
        super(accountOwner, id, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmout(getCurrentAmount() * INTEREST_RATE * 2);
    }


}
