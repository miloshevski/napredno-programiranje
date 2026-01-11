package concurent;

import java.util.*;
import java.util.concurrent.*;

public class SensorDataAnalyzer {

    static class SensorResult {
        int sensorId;
        double min;
        double max;
        double avg;

        SensorResult(int sensorId, double min, double max, double avg) {
            this.sensorId = sensorId;
            this.min = min;
            this.max = max;
            this.avg = avg;
        }
    }

    // TODO 1
    static Callable<SensorResult> analyzeSensor(int sensorId, List<Double> values) {
        return () -> {
            double min = values.stream().mapToDouble(Double::doubleValue).min().orElse(0);
            double max = values.stream().mapToDouble(Double::doubleValue).max().orElse(0);
            double avg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);

            return new SensorResult(sensorId,min,max,avg);
        };
    }

    public static void main(String[] args) throws Exception {

        // sensorId -> measurements
        Map<Integer, List<Double>> sensors = new HashMap<>();
        sensors.put(2, Arrays.asList(20.5, 21.0, 19.8, 22.1));
        sensors.put(1, Arrays.asList(30.0, 29.5, 30.2));
        sensors.put(3, Arrays.asList(15.0, 15.5));

        List<Callable<SensorResult>> tasks = new ArrayList<>();

        // TODO 2: create one Callable per sensor
        for(Map.Entry<Integer,List<Double>> e : sensors.entrySet()){
            tasks.add(analyzeSensor(e.getKey(),e.getValue()));
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        // TODO 3: execute tasks and get futures

        List<SensorResult> results = new ArrayList<>();
        List<Future<SensorResult>> futures = executor.invokeAll(tasks);

        // TODO 4: extract results from futures
        for(Future<SensorResult> f : futures){
            results.add(f.get());
        }

        executor.shutdown();

        // TODO 5: sort by sensorId

        // print
        for (SensorResult r : results) {
            System.out.printf(
                    "%d %.2f %.2f %.2f%n",
                    r.sensorId, r.min, r.max, r.avg
            );
        }
    }
}
