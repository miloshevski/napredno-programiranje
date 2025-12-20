package k1.kanvass;

import javax.management.MBeanRegistration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}
abstract class Shape implements Scalable,Stackable{
    protected String id;
    protected Color color;

    public Shape(String id, Color color){
        this.id = id;
        this.color = color;
    }
}

class Circle extends Shape{
    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.PI * radius * radius);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f\n",id,color,weight());
    }
}

class Rectangle extends Shape{
    private float width;
    private float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width *= scaleFactor;
        height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n", id, color, weight());
    }
}

enum Color {
    RED, GREEN, BLUE
}

class Canvas {
    private List<Shape> list;

    public Canvas(){
        list = new ArrayList<>();
    }

    int find(float weight) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).weight() < weight) {
                return i;
            }
        }
        return list.size();
    }
    public void add(String id, Color color, float radius) {
        Circle c = new Circle(id, color, radius);
        int index = find(c.weight());
        this.list.add(index, c);
    }

    public void add(String id, Color color, float width, float height){
        Rectangle rect = new Rectangle(id, color, width, height);
        int index = find(rect.weight());
        this.list.add(index,rect);
    }
    void scale(String id, float scaleFactor){
        Shape s = null;
        for(int i = list.size() - 1; i >= 0; i--){
            if(list.get(i).id.equals(id)){
                s = list.get(i);
                list.remove(i);
                break;
            }
        }
        s.scale(scaleFactor);
        int index = find(s.weight());
        list.add(index,s);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shape shape : list) {
            sb.append(shape);
        }
        return sb.toString();
    }

}