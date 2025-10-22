package calculator;

public class Calculator {
    private double result;
    private Strategy strategy;

    public Calculator(){
        this.result = 0.0;
    }

    public String execute(char operation, double value) throws UnknownOperationException{
        if(operation == '+'){
            strategy = new Addition();
        }else if(operation == '-'){
            strategy = new Subtraction();
        }else if(operation == '*'){
            strategy = new Multiplication();
        }else if(operation == '/'){
            strategy = new Division();
        }else{
            throw new UnknownOperationException(operation);
        }

        result = strategy.calcualte(result, value);
        return String.format("result %c %.2f", operation, value, result);
    }

    public double getResult() {
        return result;
    }

    public String init(){
        return String.format("result = %.2f", result);
    }

    @Override
    public String toString() {
        return String.format("updated result = %.2f", result);
    }


}
