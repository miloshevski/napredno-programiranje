package aud2;

import calculator.UnknownOperationException;

import java.util.Scanner;

@FunctionalInterface
interface Operation{
    double apply(double currentResult, double value);
}

class UnknownOperatorException extends Exception{
    public UnknownOperatorException(char operator){
        super(String.format("%c is an unknown operation",operator));
    }
}

class OperationFactory{
    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char MULTIPLY = '*';
    private static final char DIVIDE = '/';

    private static final Operation ADD = (r, v) -> r + v;
    private static final Operation SUBTRACT = (r, v) -> r - v;
    private static final Operation MULTIPLY_OP = (r, v) -> r * v;
    private static final Operation DIVIDE_OP = (r, v) -> r / v;

    public static Operation getOperation(char operator) throws UnknownOperatorException{
        if(operator == PLUS){
            return ADD;
        }else if(operator == MINUS){
            return SUBTRACT;
        }else if(operator == MULTIPLY){
            return MULTIPLY_OP;
        }else if(operator == DIVIDE){
            return DIVIDE_OP;
        }else {
            throw new UnknownOperatorException(operator);
        }
    }
}

class Calculator{
    private double result;

    public Calculator(){
        result = 0;
    }

    public double getResult(){
        return result;
    }

    public String init(){
        return String.format("result = %f",result);
    }

    public String execute(char operator, double value) throws UnknownOperatorException{
        Operation op = OperationFactory.getOperation(operator);
        result = op.apply(result, value);
        return String.format("result %c %f = %f", operator, value, result);
    }

    @Override
    public String toString(){
        return String.format("updated result = %f", result);
    }
}

public class CalculatorTest {
    static final char RESULT = 'r';

    static void main() {
        Scanner sc = new Scanner(System.in);

        while (true){
            Calculator calculator = new Calculator();
            System.out.println(calculator.init());

            while (true){
                String line = sc.nextLine();
                char choice = getCharLower(line);

                if(choice == RESULT){
                    System.out.printf("final result = %f%n",calculator.getResult());
                    break;
                }
                String[] parts = line.split("\\s+");
                if(parts.length < 2){
                    System.out.println("Please enter: <operator> <number>");
                    continue;
                }
                char operator = parts[0].charAt(0);
                double value = Double.parseDouble(parts[1]);
                try{
                    String result = calculator.execute(operator, value);
                    System.out.println(result);
                    System.out.println(calculator);
                }catch (UnknownOperatorException e){
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("(Y/N)");
            String again = sc.nextLine();
            char choice2 = getCharLower(again);
            if(choice2 == 'n'){
                break;
            }
        }
    }
    static char getCharLower(String line) {
        if (line.trim().length() > 0) {
            return Character.toLowerCase(line.charAt(0));
        }
        return '?';
    }
}
