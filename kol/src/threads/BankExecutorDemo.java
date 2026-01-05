package threads;

import java.security.Timestamp;
import java.sql.Time;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount {
    private int balance;
    private final Lock lock = new ReentrantLock();

    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(int amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    public int getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}



class DepositThread extends Thread{
    private final BankAccount account;
    private final int times;
    private final int amountPerDeposit;

    public DepositThread(String name, BankAccount account, int times, int amountPerDeposit){
        super(name);
        this.account = account;
        this.times = times;
        this.amountPerDeposit = amountPerDeposit;
    }

    @Override
    public void run() {
        for(int i = 0;i < times; i++){
            account.deposit(amountPerDeposit);
        }
        System.out.println(getName() + " finished");
    }
}

class DepositTask implements Runnable {

    private final BankAccount account;
    private final int times;
    private final int amount;

    public DepositTask(BankAccount account, int times, int amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            account.deposit(amount);
        }
        System.out.println(Thread.currentThread().getName() + " done");
    }
}


public class BankExecutorDemo {

    public static void main(String[] args) throws InterruptedException {

        BankAccount account = new BankAccount(0);

        int workers = 4;
        int times = 300_000;
        int amount = 1;

        int expected = workers * times * amount;

        // ✅ Thread pool со 4 нишки
        ExecutorService executor = Executors.newFixedThreadPool(workers);

        Long start = System.currentTimeMillis();
        for (int i = 0; i < workers; i++) {
            executor.submit(new DepositTask(account, times, amount));
        }

        // 🔒 затвори executor (не прима нови задачи)
        executor.shutdown();
        Long end = System.currentTimeMillis();

        // ⏳ чекај да завршат сите задачи
        executor.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println("Expected balance = " + expected);
        System.out.println("Actual balance   = " + account.getBalance());
        System.out.println(end - start);
    }
}

