package lambda;

public class AnnonymousClassExample {
    FunctionalInterface Addition = new FunctionalInterface() {
        @Override
        public double doOperation(double a, double b) {
            return a + b;
        }
    };

    public static void main(String[] args) {
        AnnonymousClassExample example = new AnnonymousClassExample();
        System.out.println(example.Addition.doOperation(5,7));
    }
}
