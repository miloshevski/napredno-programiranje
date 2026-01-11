package concurent;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TicketReservationSystem {

    // Shared resource
    static class TicketOffice {
        private int availableTickets;

        public TicketOffice(int initialTickets) {
            this.availableTickets = initialTickets;
        }


        public synchronized boolean reserve(int count) {
            if (availableTickets >= count) {
                availableTickets -= count;
                return true;
            }
            return false;
        }

        // TODO: make thread-safe
        public synchronized boolean cancel(int count) {
            availableTickets += count;
            return true;
        }

        public synchronized int getAvailableTickets() {
            return availableTickets;
        }
    }

    static class OperationResult {
        int operationId;
        boolean success;

        OperationResult(int operationId, boolean success) {
            this.operationId = operationId;
            this.success = success;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int initialTickets = sc.nextInt();
        int n = sc.nextInt();

        TicketOffice office = new TicketOffice(initialTickets);

        List<Callable<OperationResult>> tasks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String type = sc.next();
            int count = sc.nextInt();
            int operationId = i + 1;

            tasks.add(() -> {
                Thread.sleep(100); // artificial delay
                boolean success;
                if (type.equals("reserve")) {
                    success = office.reserve(count);
                } else { // cancel
                    success = office.cancel(count);
                }
                return new OperationResult(operationId, success);
            });
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<OperationResult>> futures = executor.invokeAll(tasks);

        for (Future<OperationResult> f : futures) {
            f.get();
        }

        executor.shutdown();

        System.out.println("FINAL_TICKETS " + office.getAvailableTickets());
    }
}
