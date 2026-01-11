package concurent;

class Task implements Runnable {
    private static int sharedResource = 0;
    private static final Object lock = new Object();

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            synchronized (lock) {
                sharedResource++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {
}
