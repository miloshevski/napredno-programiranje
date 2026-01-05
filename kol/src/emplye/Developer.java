package emplye;

public class Developer implements Employee{
    private String name;

    public Developer(String name){
        this.name = name;
    }

    @Override
    public void showInfo(){
        System.out.printf("Developer: %s",name);
    }
}
