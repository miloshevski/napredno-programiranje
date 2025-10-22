package calculator;

import java.util.Scanner;

public class CalculatorTest {

    public static char getChar(String s){
        return !s.trim().isEmpty() ? Character.toLowerCase(s.trim().charAt(0)) : '?';
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true){
            Calculator calculator = new Calculator();
            System.out.println(calculator.init());

            while (true){
                String line = sc.nextLine();
                char choice = getChar(line);
                if(choice == 'r'){
                    System.out.println(String.format("final result = %f", calculator.getResult()));
                    break;
                }

                String [] parts = line.split("\\s+");
                char operator = parts[0].charAt(0);
                double value = Double.parseDouble(parts[1]);

                try {
                    String result = calculator.execute(operator,value);
                    System.out.println(result);
                    System.out.println(calculator);
                } catch (UnknownOperationException e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("(Y/N)");
            String line = sc.nextLine();
            char choice = getChar(line);
            if(choice == 'n') break;
        }
    }
}
