package calculator;

public class Division implements Strategy{
    @Override
    public double calcualte(double a, double b) {
        if(b != 0){
            return a/b;
        }else {
            return a;
        }
    }
}
