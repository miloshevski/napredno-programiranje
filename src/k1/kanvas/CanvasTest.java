package k1.kanvas;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

class InvalidIDException extends Exception {
    public InvalidIDException(String id) { super(String.format("Id %s is invalid", id)); }
}
class InvalidDimensionException extends Exception {
    public InvalidDimensionException(String msg) { super(msg); }
}

abstract class Shape implements Comparable<Shape> {
    protected final String userId;
    protected Shape(String userId) throws InvalidIDException {
        if (!ID_OK.matcher(userId).matches()) throw new InvalidIDException(userId);
        this.userId = userId;
    }
    public String getUserId() { return userId; }

    public abstract double getPerimeter();
    public abstract double getArea();
    public abstract void scale(double coef);

    protected static void requireNonZero(String name, double v) throws InvalidDimensionException {
        if (v == 0.0) throw new InvalidDimensionException(name + " must be non-zero");
    }
    @Override public int compareTo(Shape o) {
        return Double.compare(this.getArea(), o.getArea()); // според плоштина (растечки)
    }

    private static final Pattern ID_OK = Pattern.compile("^[A-Za-z0-9]{6}$");
}

class Circle extends Shape {
    private double r;
    public Circle(String userId, double r) throws InvalidIDException, InvalidDimensionException {
        super(userId);
        requireNonZero("radius", r);
        this.r = r;
    }
    @Override public double getPerimeter() { return 2 * Math.PI * r; }
    @Override public double getArea() { return Math.PI * r * r; }
    @Override public void scale(double coef) { r *= coef; }
    @Override
    public String toString() {
        return String.format("Circle: -> Radius: %.2f Area: %.2f Perimeter: %.2f",
                r, getArea(), getPerimeter());
    }

}

class Square extends Shape {
    private double a;
    public Square(String userId, double a) throws InvalidIDException, InvalidDimensionException {
        super(userId);
        requireNonZero("side", a);
        this.a = a;
    }
    @Override public double getPerimeter() { return 4 * a; }
    @Override public double getArea() { return a * a; }
    @Override public void scale(double coef) { a *= coef; }
    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",
                a, getArea(), getPerimeter());
    }

}

class Rectangle extends Shape {
    private double w, h;
    public Rectangle(String userId, double w, double h) throws InvalidIDException, InvalidDimensionException {
        super(userId);
        requireNonZero("width", w);
        requireNonZero("height", h);
        this.w = w; this.h = h;
    }
    @Override public double getPerimeter() { return 2 * (w + h); }
    @Override public double getArea() { return w * h; }
    @Override public void scale(double coef) { w *= coef; h *= coef; }
    @Override
    public String toString() {
        return String.format(
                "Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",
                w, h, getArea(), getPerimeter()
        );
    }

}

class Canvas {
    private final List<Shape> shapes;

    public Canvas() { this.shapes = new ArrayList<>(); }

    // формат ред: type(1/2/3) userId [dim] [dim]
    public void readShapes(InputStream is) {
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] t = line.split("\\s+");
            try {
                int type = Integer.parseInt(t[0]);
                if (type == 1) {               // 1 uid r
                    String uid = t[1];
                    double r = Double.parseDouble(t[2]);
                    shapes.add(new Circle(uid, r));
                } else if (type == 2) {        // 2 uid a
                    String uid = t[1];
                    double a = Double.parseDouble(t[2]);
                    shapes.add(new Square(uid, a));
                } else if (type == 3) {        // 3 uid w h
                    String uid = t[1];
                    double w = Double.parseDouble(t[2]);
                    double h = Double.parseDouble(t[3]);
                    shapes.add(new Rectangle(uid, w, h));
                } // непознат type -> игнор
            } catch (InvalidIDException e) {
                // лош ID -> прескокни ја формата, продолжи да читаш
                // (не фрламе понатаму)
            } catch (InvalidDimensionException e) {
                // димензија 0 -> прекин на понатамошно читање
                break;
            } catch (Exception e) {
                // било кој друг проблем (парсирање/недостасува полиња) -> игнорирај ред
            }
        }
    }

    public void scaleShapes(String userID, double coef) {
        for (Shape s : shapes) if (s.getUserId().equals(userID)) s.scale(coef);
    }

    // формите сортирани по плоштина (растечки), без stream.sorted()
    public void printAllShapes(OutputStream os) {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
        List<Shape> copy = new ArrayList<>(shapes);
        Collections.sort(copy); // користи compareTo -> по area
        for (Shape s : copy) pw.println(s);
        pw.flush();
    }

    // групирање по user, корисници: по број форми desc, па сума плоштини desc, па userId asc
    // формите за корисник: по периметар desc; без stream.sorted() за формите
    public void printByUserId(OutputStream os) {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));

        // групирање
        Map<String, List<Shape>> byUser = new HashMap<>();
        for (Shape s : shapes) {
            byUser.computeIfAbsent(s.getUserId(), k -> new ArrayList<>()).add(s);
        }

        // листа на корисници за сортирање
        List<Map.Entry<String, List<Shape>>> users = new ArrayList<>(byUser.entrySet());

        // корисници сортирани по (#форми desc, сума area desc, userId asc)
        users.sort((e1, e2) -> {
            int c1 = e1.getValue().size();
            int c2 = e2.getValue().size();
            if (c1 != c2) return Integer.compare(c2, c1); // desc
            double a1 = e1.getValue().stream().mapToDouble(Shape::getArea).sum();
            double a2 = e2.getValue().stream().mapToDouble(Shape::getArea).sum();
            int cmpA = Double.compare(a2, a1); // desc
            if (cmpA != 0) return cmpA;
            return e1.getKey().compareTo(e2.getKey()); // asc
        });

        for (Map.Entry<String, List<Shape>> e : users) {
            String uid = e.getKey();
            List<Shape> list = e.getValue();

            int count = list.size();
            double sumArea = 0;
            for (Shape s : list) sumArea += s.getArea();
            pw.printf(Locale.US, "User %s -> count=%d, sumArea=%.2f%n", uid, count, sumArea);

            // сортирање на формите по периметар desc (без stream.sorted)
            list.sort((s1, s2) -> Double.compare(s2.getPerimeter(), s1.getPerimeter()));
            for (Shape s : list) pw.println("  " + s);
        }
        pw.flush();
    }

    // статистика за плоштини
    public void statistics(OutputStream os) {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
        long cnt = shapes.size();
        pw.printf(Locale.US, "count:\t%d%n", cnt);
        if (cnt > 0) {
            double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY, sum = 0;
            for (Shape s : shapes) {
                double a = s.getArea();
                sum += a;
                if (a < min) min = a;
                if (a > max) max = a;
            }
            double avg = sum / cnt;
            pw.printf(Locale.US, "min:\t%.3f%n", min);
            pw.printf(Locale.US, "max:\t%.3f%n", max);
            pw.printf(Locale.US, "sum:\t%.3f%n", sum);
            pw.printf(Locale.US, "avg:\t%.3f%n", avg);
        }
        pw.flush();
    }
}

// пример тест-харнес од задачата
public class CanvasTest {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        canvas.readShapes(System.in);

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}
