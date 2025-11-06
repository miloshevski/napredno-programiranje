package k1.shapetester;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Canvas implements Comparable<Canvas> {
    private final String ID;
    private final List<Integer> size;

    public Canvas(String str) {
        this.size = new ArrayList<Integer>();

        String[] split = str.split("\\s+");
        this.ID = split[0];

        for (int i = 1; i < split.length; i++) {
            size.add(Integer.parseInt(split[i]));
        }
    }

    public int getPerimeter() {
        int s = 0;

        for (Integer i : size) {
            s += 4 * i;
        }

        return s;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", ID, size.size(), getPerimeter());
    }

    @Override
    public int compareTo(Canvas o) {
        return getPerimeter() - o.getPerimeter();
    }
}

class ShapesApplication{
    private List<Canvas> list;

    public ShapesApplication(){
        this.list = new ArrayList<>();
    }
    public int readCanvases(InputStream inputStream){
        Scanner sc = new Scanner(System.in);
        String str;
        int s = 0;

        while (sc.hasNextLine()){
            str = sc.nextLine();
            s += str.split("\\s++").length - 1;
            list.add(new Canvas(str));
        }
        return s;
    }
    public void printLargestCanvasTo(OutputStream outputStream){
        System.out.println(list.stream().max(Canvas::compareTo).get());
    }
}
public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}