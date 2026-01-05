package emplye;

public class Designer implements Employee{
    private String name;

    public Designer(String name) {
        this.name = name;
    }

    @Override
    public void showInfo() {
        System.out.printf("Designer: %s",name);
    }
}
