package concurent;

import java.util.*;
import java.util.concurrent.*;

public class OrderProcessing {

    /**
     * Оваа класа го претставува РЕЗУЛТАТОТ од една задача.
     * Секој Callable ќе врати еден OrderResult.
     */
    static class OrderResult {
        int orderId;       // идентификатор на нарачката
        double totalPrice; // вкупна цена за нарачката

        OrderResult(int orderId, double totalPrice) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
        }

        @Override
        public String toString() {
            return "Order " + orderId + " total = " + totalPrice;
        }
    }

    /**
     * Овој метод КРЕИРА Callable задача.
     * ❗ Забелешка: ОВДЕ НЕ СЕ ИЗВРШУВА пресметката,
     * туку само се подготвува задача што ќе се изврши подоцна во thread.
     */
    static Callable<OrderResult> processOrder(int orderId, List<Double> prices) {

        // Lambda израз што имплементира Callable<OrderResult>
        return () -> {

            // Овој код ќе се извршува ВО THREAD
            double sum = 0;

            // Пресметка на вкупната цена
            for (double p : prices) {
                sum += p;
            }

            // Симулираме "скапа" операција (пример: комуникација со сервис)
            Thread.sleep(500);

            // Секој Callable МОРА да врати резултат
            return new OrderResult(orderId, sum);
        };
    }


    public static void main(String[] args) throws Exception {

        /**
         * Имаме повеќе нарачки.
         * Map<orderId, листа од цени>
         */
        Map<Integer, List<Double>> orders = new HashMap<>();
        orders.put(3, Arrays.asList(100.0, 50.0, 20.0));
        orders.put(1, Arrays.asList(10.0, 40.0));
        orders.put(2, Arrays.asList(200.0));

        /**
         * Листа од задачи (НЕ од резултати!)
         * Секоја задача е Callable<OrderResult>
         */
        List<Callable<OrderResult>> tasks = new ArrayList<>();

        // 1) За секоја нарачка креираме ТОЧНО една Callable задача
        for (Map.Entry<Integer, List<Double>> e : orders.entrySet()) {
            tasks.add(processOrder(e.getKey(), e.getValue()));
        }

        /**
         * ExecutorService = Thread Pool
         * Креираме 2 threads што ќе ги извршуваат задачите
         */
        ExecutorService executor = Executors.newFixedThreadPool(2);

        /**
         * 2) invokeAll:
         * - ги стартува СИТЕ Callables
         * - чека додека СИТЕ не завршат
         * - враќа List<Future<OrderResult>>
         */
        List<Future<OrderResult>> futures = executor.invokeAll(tasks);

        /**
         * Овде ќе ги собереме резултатите
         */
        List<OrderResult> results = new ArrayList<>();

        /**
         * 3) Future.get():
         * - ако резултатот не е готов → чека
         * - ако е готов → го враќа OrderResult
         */
        for (Future<OrderResult> f : futures) {
            results.add(f.get());
        }

        // Го гасиме executor-от (многу важно!)
        executor.shutdown();

        /**
         * Поради паралелно извршување,
         * редоследот НЕ Е гарантиран → затоа сортираме
         */
        results.sort(Comparator.comparingInt(o -> o.orderId));

        // Финален излез
        for (OrderResult r : results) {
            System.out.println(r);
        }
    }
}
