package calculator;

public class Subtraction implements Strategy{
    @Override
    public double calcualte(double a, double b) {
        return a-b;
    }
}
