package av3;

public class CannotWithdrawMoneyException extends Exception{

    public CannotWithdrawMoneyException(double currentAmount, double amount) {
        super(String.format("Your current amount is: %.2f, and you can not withdraw this amount: %.2f",currentAmount,amount));
    }


}
