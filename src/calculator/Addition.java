package calculator;

public class Addition implements Strategy{

    @Override
    public double calcualte(double a, double b) {
        return a + b;
    }
}
