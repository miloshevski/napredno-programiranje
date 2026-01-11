package concurent;

import java.util.ArrayList;
import java.util.List;

public class BankAccountSimulator {

    static class Printer extends Thread{
        private int num;

        public Printer(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            System.out.println(num);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        List<Printer> list = new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add(new Printer(i));
        }
        list.forEach(Thread::start);
        for (Printer printer : list) {
            printer.join();
        }
    }
}
