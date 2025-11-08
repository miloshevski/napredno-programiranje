package k1.komponenta;

import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}
class InvalidPositionException extends Exception{
    public InvalidPositionException(int pos) {
        super(String.format("Invalid position %d, alredy taken!",pos));
    }
}

class Component implements Comparable<Component>{
    private String color;
    private final int weight;
    private final Set<Component> set;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.set = new TreeSet<>();
    }

    public void addComponent(Component component){
        set.add(component);
    }

    public int getWeight() {
        return weight;
    }

    public void setColor(String color){
        this.color = color;
    }

    public Set<Component> getSet() {
        return set;
    }

    @Override
    public String toString() {
        return String.format("%d:%s", weight, color);
    }

    @Override
    public int compareTo(Component o) {
        int compare = weight - o.weight;
        return compare != 0 ? compare : color.compareTo(o.color);
    }
}

class Window{
    private final String name;
    private final Map<Integer, Component> map;

    public Window(String name){
        this.name = name;
        this.map = new TreeMap<>();
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        if(map.containsKey(position)){
            throw new InvalidPositionException(position);
        }
        map.put(position,component);
    }

    public void changeColor(int weight, String color){
        for(Component component: map.values()){
            changeColorRecursive(weight, color, component);
        }
    }

    private void changeColorRecursive(int weight, String color, Component component){
        if(component.getWeight() < weight){
            component.setColor(color);
        }
        for(Component c : component.getSet()){
            changeColorRecursive(weight,color,c);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("WINDOW ").append(name).append("\n");
        map.forEach((k, v) -> sb.append(String.format("%d:%s", k, buildString(v, 0))));

        return sb.toString();
    }
    private String buildString(Component component, int level) {
        StringBuilder sb = new StringBuilder();
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < level * 3; i++) {
            str.append("-");
        }

        sb.append(String.format("%s%s%n", str, component));

        for (Component c : component.getSet()) {
            sb.append(buildString(c, level + 1));
        }

        return sb.toString();
    }
    public void swichComponents(int pos1, int pos2){
        Component a = map.get(pos1);
        Component b = map.get(pos2);
        map.put(pos1,b);
        map.put(pos2,a);
    }
}