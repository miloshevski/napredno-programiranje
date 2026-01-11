package concurent;

import java.util.*;
import java.util.concurrent.*;

public class FileProcessingSystem {

    static class FileLoader implements Runnable {

        private final String fileName;
        private final List<String> loadedFiles;

        public FileLoader(String fileName, List<String> loadedFiles) {
            this.fileName = fileName;
            this.loadedFiles = loadedFiles;
        }

        @Override
        public void run() {
            try {
                // simulate file loading
                Thread.sleep(100);

                // thread-safe add to shared list
                synchronized (loadedFiles) {
                    loadedFiles.add(fileName);
                }

            } catch (InterruptedException e) {
                // restore interrupt flag (good practice)
                Thread.currentThread().interrupt();
            }
        }
    }

    // Callable with lambda: analyze file and return "size" (here: filename length)
    static Callable<Integer> analyzeFile(String fileName) {
        return () -> {
            // simulate heavy analysis
            Thread.sleep(200);
            return fileName.length();
        };
    }

    public static void main(String[] args) throws Exception {

        List<String> files = Arrays.asList(
                "data.txt",
                "image.png",
                "report.pdf",
                "notes.docx"
        );

        // shared list (not thread-safe by default -> we synchronize when adding)
        List<String> loadedFiles = new ArrayList<>();

        List<Thread> loaderThreads = new ArrayList<>();

        // Start threads for loading files (Runnable + Thread)
        for (String f : files) {
            Thread t = new Thread(new FileLoader(f, loadedFiles));
            loaderThreads.add(t);
            t.start();
        }

        // Join all loader threads (wait for loading to finish)
        for (Thread t : loaderThreads) {
            t.join();
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);

        List<Callable<Integer>> tasks = new ArrayList<>();

        // Create Callable tasks for loaded files
        for (String lf : loadedFiles) {
            tasks.add(analyzeFile(lf));
        }

        // Execute tasks and get futures
        List<Future<Integer>> futures = executor.invokeAll(tasks);

        // Extract results and print
        // (order of completion is not guaranteed; invokeAll returns futures in task order)
        for (int i = 0; i < loadedFiles.size(); i++) {
            String fileName = loadedFiles.get(i);
            int analyzedValue = futures.get(i).get();
            System.out.println(fileName + " -> analyzed value: " + analyzedValue);
        }

        executor.shutdown();
    }
}
