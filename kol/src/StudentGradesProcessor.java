import java.util.*;
import java.util.concurrent.*;

public class StudentGradesProcessor {

    static class StudentResult {
        int studentId;
        double average;
        int passed;
        int failed;

        StudentResult(int studentId, double average, int passed, int failed) {
            this.studentId = studentId;
            this.average = average;
            this.passed = passed;
            this.failed = failed;
        }
    }

    static Callable<StudentResult> processStudent(int studentId, List<Integer> grades) {
        return () -> {
            int passed = 0, failed = 0;
            int sum = 0;

            for(int g : grades){
                sum +=g;
                if(g >= 6){
                    passed++;
                }else {
                    failed++;
                }
            }
            double avg = grades.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
            return new StudentResult(studentId,avg,passed,failed);
        };
    }

    public static void main(String[] args) throws Exception {

        // studentId -> list of grades
        Map<Integer, List<Integer>> students = new HashMap<>();
        students.put(3, Arrays.asList(10, 9, 8, 6));
        students.put(1, Arrays.asList(5, 6, 7));
        students.put(2, Arrays.asList(6, 6, 6, 6));

        List<Callable<StudentResult>> tasks = new ArrayList<>();

        for(Map.Entry<Integer,List<Integer>> e : students.entrySet()){
            tasks.add(processStudent(e.getKey(),e.getValue()));
        }


        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        // TODO 3: execute all tasks and get List<Future<StudentResult>>
        List<Future<StudentResult>> futures = executor.invokeAll(tasks);

        List<StudentResult> results = new ArrayList<>();

        // TODO 4: extract results from futures
        for(Future<StudentResult> f : futures){
            results.add(f.get());
        }

        executor.shutdown();

        // TODO 5: sort results by studentId
        results.sort(Comparator.comparingInt(r -> r.studentId));

        // print results
        for (StudentResult r : results) {
            System.out.printf(
                    "%d %.2f %d %d%n",
                    r.studentId, r.average, r.passed, r.failed
            );
        }
    }
}
