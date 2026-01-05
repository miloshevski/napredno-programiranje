package emplye;

import java.util.ArrayList;
import java.util.List;

public class Manager implements Employee{
    private String name;
    private List<Employee> team = new ArrayList<>();

    public Manager(String name) {
        this.name = name;
    }

    public void addEmployee(Employee e) {
        team.add(e);
    }

    public void removeEmployee(Employee e) {
        team.remove(e);
    }

    @Override
    public void showInfo() {
        System.out.printf("Manager: %s%n",name);
        for(Employee e : team){
            e.showInfo();
        }
    }
}
