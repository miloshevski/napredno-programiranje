package xmltest;

import java.util.ArrayList;
import java.util.List;

interface Task{
    String getName();
    String description();
}

class SimpleTask implements Task{
    private String name;

    public SimpleTask(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String description() {
        return "    This is task " + name + " description.\n";
    }
}

class TaskList implements Task{
    private String name;
    private final List<Task> list;

    public TaskList(String name) {
        this.name = name;
        list = new ArrayList<>();
    }

    public void addTask(Task t){
        list.add(t);
    }

    public String print(){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        for(Task t : list){
            sb.append("    ").append(t.description());
        }
        return sb.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String description() {
        return print();
    }
}


public class CompositeTask {
    static void main() {
        SimpleTask simpleTask = new SimpleTask("Simple 1");
        SimpleTask simpleTask2 = new SimpleTask("Simple 2");

        TaskList taskList = new TaskList("Task List");
        taskList.addTask(simpleTask);
        taskList.addTask(simpleTask2);

        TaskList taskList1 = new TaskList("Task List2");
        taskList1.addTask(simpleTask);
        taskList1.addTask(simpleTask2);
        taskList1.addTask(taskList);

        TaskList taskList2 = new TaskList("TRI");
        taskList2.addTask(taskList1);
        System.out.println(taskList2.print());
    }
}
