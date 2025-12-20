package k1.studentrekords;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class Nasoka{
    private String name;
    private Map<Integer,Integer> grades;

    public Nasoka(String name){
        this.name = name;
        this.grades = new TreeMap<>();
    }
    public void addGrade(int grade){
        grades.put(grade,grades.getOrDefault(grade,0) + 1);
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Integer> getGrades() {
        return grades;
    }
    public String getStars(int grade) {
        int count = grades.getOrDefault(grade, 0);
        int stars = (int) Math.floor(count / 10.0) + (count % 10 != 0 ? 1 : 0);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stars; i++) {
            sb.append('*');
        }
        return sb.toString();
    }

    public int getDesetki(){
        return grades.getOrDefault(10,0);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append('\n');
        for (int i = 6; i <= 10; i++) {
            int cnt = grades.getOrDefault(i, 0);
            sb.append(String.format("%2d | %s(%d)%n", i, getStars(i), cnt));
        }
        return sb.toString();
    }
}

class Student{
    private String id;
    private String nasoka;
    private List<Integer> grades;
    public Student(String id,String nasoka,List<Integer> grades){
        this.id = id;
        this.nasoka = nasoka;
        this.grades = grades;
    }
    public double average(){
        return grades.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
    }

    public String getId() {
        return id;
    }

    public String getNasoka() {
        return nasoka;
    }

    public List<Integer> getGrades() {
        return grades;
    }

}

class StudentRecords{
    private List<Student> students;
    private Map<String,List<Student>> map;
    private Map<String, Nasoka> nasoki;
    public StudentRecords(){
        this.students = new ArrayList<>();
        this.map = new TreeMap<>();
        this.nasoki = new TreeMap<>();
    }

    public int readRecords(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        int c= 0;
        while (sc.hasNextLine()){
            String [] line = sc.nextLine().split("\\s++");
            String code = line[0];
            String nasoka = line[1];
            List<Integer> grades = new ArrayList<>();
            Nasoka nas = nasoki.getOrDefault(nasoka,new Nasoka(nasoka));
            for(int i=2;i<line.length;i++){
                int grade = Integer.parseInt(line[i]);
                grades.add(grade);
                nas.addGrade(grade);
            }
            Student s = new Student(code,nasoka,grades);
            students.add(s);
            if(!nasoki.containsKey(nasoka)){
                nasoki.put(nasoka,nas);
            }
            map.computeIfAbsent(nasoka,k -> new ArrayList<>()).add(s);
            c++;
        }
        return c;
    }
    public void writeTable(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));

        for(Map.Entry<String,List<Student>> e : map.entrySet()){
            String nasoka = e.getKey();
            List<Student> list = e.getValue();

            list.sort(Comparator.comparingDouble(Student::average).reversed().thenComparing(Student::getId));
            pw.println(nasoka);
            for(Student s : list){
                pw.printf("%s %.2f%n", s.getId(), s.average());
            }
        }
        pw.flush();

    }
    public void writeDistribution(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));

        List<Nasoka> list = nasoki.values().stream().collect(Collectors.toList());
        list.stream().sorted(Comparator.comparingInt(Nasoka::getDesetki).reversed()).forEach(n -> pw.print(n.toString()));
        pw.flush();
    }
}

public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here