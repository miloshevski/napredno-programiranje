package organization;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

interface OrgComponent{
    String getName();
    long getMonthlyCost();
    String getInfo(int level);
    void sortbyCost();
    long findHighestSalary();
}

class Employee implements OrgComponent{
    protected String name;
    protected long salary;

    public Employee(String name, long salary) {
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getMonthlyCost() {
        return salary;
    }


    @Override
    public String getInfo(int level) {
        return String.format("%sEmployee: %5s Salary: %5d%n",Indent.printIndent(level),name,salary);
    }

    @Override
    public void sortbyCost() {
        return;
    }

    @Override
    public long findHighestSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return getInfo(0);
    }
}

class Indent{
    public static String printIndent(int level){
        return "    ".repeat(level);
    }
}

class Team implements OrgComponent{
    private String name;
    private long teamBudgedBonus;
    private List<OrgComponent> members;

    public Team(String name, long teamBudgedBonus) {
        this.name = name;
        this.teamBudgedBonus = teamBudgedBonus;
        members = new ArrayList<>();
    }

    public void addMember(OrgComponent c){
        members.add(c);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getMonthlyCost() {
        return teamBudgedBonus + members.stream().mapToLong(OrgComponent::getMonthlyCost).sum();
    }

    @Override
    public String getInfo(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Team: %5s Cost: %5d%n",name,getMonthlyCost()));
        sb.append(String.format("Bonus: %8d%n",teamBudgedBonus));
        for(OrgComponent c : members){
            sb.append(c.getInfo(level + 1));
        }
        return sb.toString();
    }

    @Override
    public void sortbyCost() {
        members = members.stream().sorted(Comparator.comparing(OrgComponent::getMonthlyCost)).toList();
    }

    @Override
    public long findHighestSalary() {
        return members.stream().mapToLong(OrgComponent::findHighestSalary).max().orElse(0);
    }
}

class Company{
    private final Team rootTeam;

    public Company(){
        rootTeam = new Team("ROOT",0);
    }

    public void addToRoot(OrgComponent c){
        rootTeam.addMember(c);
    }

    public long findHighestSalary(){
        return rootTeam.findHighestSalary();
    }

    public void sortByCost(){
        rootTeam.sortbyCost();
    }

    public void printStructure(){
        System.out.println(rootTeam.getInfo(0));
    }
}

public class Main {
    static void main() {
        Company company = new Company();

        Employee employee = new Employee("Emp1",1000);
        Employee employee2 = new Employee("Emp2",2000);
        Employee employee3 = new Employee("Emp3",3000);

        Team team = new Team("Team1",3000);
        team.addMember(employee);
        team.addMember(employee2);
        team.addMember(employee3);

        company.addToRoot(employee2);
        company.addToRoot(employee3);
        company.addToRoot(team);
        company.addToRoot(employee);
        company.addToRoot(employee2);
        Team t = new Team("Team2",1000);
        team.addMember(t);
        company.addToRoot(team);
        company.printStructure();
    }
}
