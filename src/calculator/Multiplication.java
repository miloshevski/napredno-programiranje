package calculator;

public class Multiplication implements Strategy{
    @Override
    public double calcualte(double a, double b) {
        return a * b;
    }
}
