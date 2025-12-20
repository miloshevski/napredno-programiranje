package student;

import java.util.*;
import java.util.stream.Collectors;

class Student{
    private String index;
    List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public double avgPoints(){
        return points.stream().mapToDouble(Integer::doubleValue).sum() / 10.0;
    }

    public String getIndex() {
        return index;
    }
    public int getLabs(){
        return points.size();
    }
    public int getYear(){
        return 20 - Integer.parseInt(index.substring(0,2));
    }
    public String isPassed(){
        return points.size() >= 8 ? "YES" : "NO";
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f",index,isPassed(),avgPoints());
    }
}

class LabExercises{
    private List<Student> students;

    public LabExercises() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student){
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        Comparator<Student> cmp;
        if (ascending) {
            cmp = Comparator
                    .comparingDouble(Student::avgPoints)
                    .thenComparing(Student::getIndex);
        } else {
            cmp = Comparator
                    .comparingDouble(Student::avgPoints)
                    .thenComparing(Student::getIndex).reversed();
        }

        students.stream()
                .sorted(cmp)
                .limit(n)
                .forEach(System.out::println);
    }

    public List<Student> failedStudents(){
        return students.stream().filter(s -> s.getLabs() < 8).sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::avgPoints)).collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::getYear,
                        TreeMap::new,
                        Collectors.averagingDouble(Student::avgPoints)
                ));
    }

}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
