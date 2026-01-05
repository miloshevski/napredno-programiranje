package emplye;

public class Main {
    public static void main(String[] args) {

        Employee dev1 = new Developer("Ana");
        Employee dev2 = new Developer("Marko");
        Employee des1 = new Designer("Elena");

        Manager techLead = new Manager("Ivan");
        techLead.addEmployee(dev1);
        techLead.addEmployee(dev2);

        Manager head = new Manager("Petar");
        head.addEmployee(techLead);
        head.addEmployee(des1);

        head.showInfo();
    }
}
