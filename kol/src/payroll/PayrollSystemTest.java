package payroll;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

abstract class Employee{
    private String type;
    private String id;
    private String level;

    public Employee(String type, String id, String level) {
        this.type = type;
        this.id = id;
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }
    abstract double salary(Map<String,Double> hourlyRateByLevel,Map<String, Double> ticketRateByLevel);
}

class HourlyEmployee extends Employee{
    private int hours;

    public HourlyEmployee(String type, String id, String level, int hours) {
        super(type, id, level);
        this.hours = hours;
    }

    public int getHours() {
        return hours;
    }

    @Override
    double salary(Map<String,Double> hourlyRateByLevel,Map<String, Double> ticketRateByLevel) {
        return hours <= 40 ? hours * hourlyRateByLevel.get(getLevel()) : hourlyRateByLevel.get(getLevel())*(40 + (hours - 40) * 1.5);
    }
}
class FreelanceEmployee extends Employee{
    List<Integer> points;

    public FreelanceEmployee(String type, String id, String level, List<Integer> points) {
        super(type, id, level);
        this.points = points;
    }

    @Override
    double salary(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        return points.stream().mapToDouble(Integer::doubleValue).sum() * ticketRateByLevel.get(getLevel());
    }
}

class PayrollSystem{
    private Map<String,Double> hourlyRateByLevel;
    private Map<String, Double> ticketRateByLevel;
    private List<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        this.employees = new ArrayList<>();
    }

    public void readEmployees(InputStream is){
        Scanner sc = new Scanner(is);
        while (sc.hasNext()){
            String[]line = sc.nextLine().split(";");
            if(line[0].equals("H")){
                employees.add(new HourlyEmployee("H",line[1],line[2],Integer.parseInt(line[3])));
            }else{
                List<Integer> points = Arrays.stream(line).skip(3).map(Integer::parseInt).collect(Collectors.toList());
                employees.add(new FreelanceEmployee("F",line[1],line[2],points));
            }
        }
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        PrintWriter pw = new PrintWriter(os);

        // Компаратор: прво плата (desc), па id (за стабилен редослед)
        Comparator<Employee> bySalaryDescThenId =
                Comparator.comparingDouble(
                                (Employee e) -> e.salary(hourlyRateByLevel, ticketRateByLevel))
                        .reversed()
                        .thenComparing(Employee::getId);

        // 1) Групирање по ниво, но само за нивата што ги бара сетот "levels"
        Map<String, List<Employee>> grouped = employees.stream()
                .filter(e -> levels.contains(e.getLevel()))
                .collect(Collectors.groupingBy(Employee::getLevel));

        // 2) Резултат мапа – сакаме редоследот на клучевите да е како во "levels"
        Map<String, Set<Employee>> result = new LinkedHashMap<>();

        // 3) Итерираме по "levels" за да го задржиме редоследот
        for (String level : levels) {
            List<Employee> listForLevel = grouped.getOrDefault(level, new ArrayList<>());

            // Сортирање во рамки на нивото
            listForLevel.sort(bySalaryDescThenId);

            // Го ставаме во LinkedHashSet за да се задржи сортираниот редослед
            Set<Employee> setForLevel = new LinkedHashSet<>(listForLevel);
            result.put(level, setForLevel);

            // Печатење на излезниот поток
            pw.println("LEVEL: " + level);
            pw.println("Employees:");
            for (Employee e : listForLevel) {
                pw.printf("ID: %s TYPE: %s LEVEL: %s SALARY: %.2f%n",
                        e.getId(),
                        e.getType(),
                        e.getLevel(),
                        e.salary(hourlyRateByLevel, ticketRateByLevel));
            }
        }

        pw.flush();
        return result;
    }

}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
        });


    }
}
