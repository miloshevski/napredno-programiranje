package av3;

public class NonInterestCheckingAccount extends Account{

    public NonInterestCheckingAccount(String accountOwner, int id, double currentAmount) {
        super(accountOwner, id, currentAmount);
    }

    @Override
    public AccountType getAccountType() {
        return getAccountType().NON_INTEREST;
    }

}
