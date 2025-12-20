package k1.klaster;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

abstract class Clusterable<T>{
    protected long ID;
    public abstract long getID();
    public abstract double getDistance(T e);
}



class Cluster<T extends Clusterable<T>>{
    private final Map<Long,T> elements;
    public Cluster(){
        this.elements = new HashMap<>();
    }

    public void addItem(T e){
        elements.put(e.getID(),e);
    }

    public void near(long ID, int top){
        T e = elements.get(ID);
        Set<T> set = new TreeSet<>(Comparator.comparingDouble(a -> a.getDistance(e)));
        set.addAll(elements.values());
        set.remove(e);
        AtomicInteger i = new AtomicInteger(1);
        set.stream().limit(top).forEach(element -> System.out.printf("%d. %d -> %.3f%n", i.getAndIncrement(), element.getID(), element.getDistance(e)));
    }
}
class Point2D extends Clusterable<Point2D> {
    private final double x;
    private final double y;

    public Point2D(long ID, double x, double y) {
        this.ID = ID;
        this.x = x;
        this.y = y;
    }

    @Override
    public double getDistance(Point2D e) {
        return Math.sqrt(Math.pow(x - e.getX(), 2) + Math.pow(y - e.getY(), 2));
    }

    public long getID() {
        return ID;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here
