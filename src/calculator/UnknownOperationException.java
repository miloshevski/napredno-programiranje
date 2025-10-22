package calculator;

public class UnknownOperationException extends Exception {
    public UnknownOperationException(char operator){
        super(String.format("This operator %c is not valid.",operator));
    }

}
