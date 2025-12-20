package k1.fakultettest;
//package mk.ukim.finki.vtor_kolokvium;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class OperationNotAllowedException extends Exception{
    public OperationNotAllowedException(String message) {
        super(message);
    }
}

class Student{
    private String id;
    private int yearsOfStudies;
    private Map<Integer,Map<String,List<Integer>>> map = new HashMap<>();
    private boolean diplomiran;
    private int polozeni;
    private Set<String> predmeti = new HashSet<>();

    public Student(String id, int yearsOfStudies) {
        this.id = id;
        this.yearsOfStudies = yearsOfStudies;
        this.diplomiran = false;
        this.polozeni = 0;
    }

    public int getPolozeni() {
        return polozeni;
    }

    public double getAverageGrade(){
       return map.values().stream()
                .flatMap(m -> m.values().stream())
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }



    private boolean checkDiplomiran(){
        if((yearsOfStudies == 3 && polozeni >= 18) || (yearsOfStudies == 4 && polozeni >= 24)){
            this.diplomiran = true;
        }
        return diplomiran;
    }

    public void addGrade(int term, String courseName,int grade) throws OperationNotAllowedException {
        if((yearsOfStudies == 3 && term > 6) || (yearsOfStudies == 4 && term > 8)){
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s",term,id));
        }
        Map<String,List<Integer>> termMap = map.computeIfAbsent(term, t -> new HashMap<>());
        int totalInTerm = termMap.values().stream().mapToInt(List::size).sum();
        if(totalInTerm >= 3){
            throw new OperationNotAllowedException(
                    String.format("Student %s already has 3 grades in term %d", id, term)
            );
        }
        termMap.computeIfAbsent(courseName, c -> new ArrayList<>()).add(grade);
        predmeti.add(courseName);
        this.polozeni++;
        checkDiplomiran();
    }

    public Set<String> getPredmeti() {
        return predmeti;
    }

    public boolean isDiplomiran() {
        return diplomiran;
    }

    public String getId() {
        return id;
    }
    public double avgForTerm(int n){
        return map.get(n).values().stream()
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
    public double avgGrade(){
        return map.values().stream()
                .flatMap(m -> m.values().stream())
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public int getYearsOfStudies() {
        return yearsOfStudies;
    }
    public int getNumberOfCoursesFromTerm(int term){
        return map.get(term).size();
    }
    private int getTotalCoursesAttended() {
        return (int) map.values().stream()
                .flatMap(m -> m.keySet().stream())
                .distinct()
                .count();
    }
    private String getAllCoursesSorted() {
        return map.values().stream()
                .flatMap(m -> m.keySet().stream())      // Stream<String>
                .distinct()
                .sorted()                               // lexicographic sort
                .collect(Collectors.joining(", "));     // combine into "course1, course2, ..."
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student: %s%n",id));
        for(int i = 1;i<=yearsOfStudies;i++){
            sb.append(String.format("Term %d:%n",i));
            sb.append(String.format("Courses for term: %d%n ",getNumberOfCoursesFromTerm(i)));
            sb.append(String.format("Average grade for term: %.2f%n",avgForTerm(i)));
        }
        sb.append(String.format("Average grade: %.2f%n",avgGrade()));
        sb.append(String.format("Courses attended: %d%n",getTotalCoursesAttended()));
        sb.append(String.format("Courses attended: %s%n",getAllCoursesSorted()));
        return sb.toString();
    }
}

class Faculty {
    private Map<String,Student> studenti;
    private StringBuilder sb = new StringBuilder();

    public Faculty() {
        this.studenti = new HashMap<>();
    }


    void addStudent(String id, int yearsOfStudies) {
        studenti.computeIfAbsent(id, k -> new Student(id, yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        Student s = studenti.get(studentId);
        s.addGrade(term,courseName,grade);
        if(s.isDiplomiran()){
            sb.append(String.format("Student with ID %s graduated with average grade %.2f in %d years.",s.getId(),s.getAverageGrade(),s.getYearsOfStudies())).append("\n");
            studenti.remove(s);
        }
    }
    public String getFacultyLogs(){
        return sb.toString();
    }

    String getDetailedReportForStudent(String id) {
        Student s = studenti.get(id);
        return s.toString();
    }

    void printFirstNStudents(int n) {
        studenti.values().stream().sorted(Comparator.comparingInt(Student::getPolozeni).thenComparing(Student::getAverageGrade).reversed()).limit(n).forEach(System.out::println);
    }

    void printCourses() {
        Set<String> predmeti = new HashSet<>();
        studenti.values().stream()
                .forEach(s -> {
                    s.getPredmeti().stream().forEach(p -> predmeti.add(p));
                });
        predmeti.stream().forEach(System.out::println);
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
