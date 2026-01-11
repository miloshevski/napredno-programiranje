package concurent;

import java.util.*;
import java.util.concurrent.*;

public class OnlineExamSystem {

    static class StudentSubmission implements Runnable {

        private final int score;
        private final List<Integer> submittedScores;

        public StudentSubmission(int score, List<Integer> submittedScores) {
            this.score = score;
            this.submittedScores = submittedScores;
        }

        @Override
        public void run() {
            try {
                // simulate submission
                Thread.sleep(100);

                // add score thread-safe to shared list
                synchronized (submittedScores) {
                    submittedScores.add(score);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static Callable<String> processScore(int score) {
        return () -> {
            Thread.sleep(150); // simulate processing
            return score >= 50 ? "PASS" : "FAIL";
        };
    }

    public static void main(String[] args) throws Exception {

        List<Integer> scores = Arrays.asList(85, 40, 67, 30, 90);

        List<Integer> submittedScores = new ArrayList<>();
        List<Thread> submissionThreads = new ArrayList<>();

        // start submission threads (Runnable + Thread)
        for (int s : scores) {
            Thread t = new Thread(new StudentSubmission(s, submittedScores));
            submissionThreads.add(t);
            t.start();
        }

        // join all submission threads
        for (Thread t : submissionThreads) {
            t.join();
        }

        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<Callable<String>> tasks = new ArrayList<>();

        // create callable tasks from submittedScores
        for (int s : submittedScores) {
            tasks.add(processScore(s));
        }

        // execute tasks and get futures
        List<Future<String>> futures = executor.invokeAll(tasks);

        // extract and print results (in submission order)
        for (int i = 0; i < submittedScores.size(); i++) {
            int score = submittedScores.get(i);
            String status = futures.get(i).get();
            System.out.println(score + " -> " + status);
        }

        executor.shutdown();
    }
}
