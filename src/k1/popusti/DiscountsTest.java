package k1.popusti;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Product{
    private int cena;
    private int cenapopust;

    public Product(int cenapopust, int cena) {
        this.cena = cena;
        this.cenapopust = cenapopust;
    }

    public int getCena() {
        return cena;
    }

    public int getCenapopust() {
        return cenapopust;
    }
    public int getPopust(){
        return cena - cenapopust;
    }
    public int getProcent(){
        double diff = cena - cenapopust;
        diff = (diff*100) / cena;
        return (int)diff;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d",getProcent(),getCenapopust(),getCena());
    }
}
class Discounts{
    List<Store> list;
    public Discounts(){
        list = new ArrayList<>();
    }

    public int readStores(InputStream inputStream){
        int c = 0;
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()){
            String[] line = sc.nextLine().split("\\s+");
            String name = line[0];
            Store store = new Store(name);
            for(int i=1;i<line.length;i++){
                int popust = Integer.parseInt(line[i].split(":")[0]);
                int cena = Integer.parseInt(line[i].split(":")[1]);
                Product p = new Product(popust,cena);
                store.addProduct(p);
            }
            list.add(store);
            c++;
        }
        return c;
    }

    public List<Store> byAverageDiscount(){
        return list.stream().sorted(Comparator.comparing(Store::averageDiscount).thenComparing(Store::getName).reversed()).limit(3).collect(Collectors.toList());
    }
    public List<Store> byTotalDiscount(){
        return list.stream().sorted(Comparator.comparing(Store::sumDis).thenComparing(Store::getName)).limit(3).collect(Collectors.toList());
    }
}
class Store{
    private String name;
    private List<Product> list;

    public Store(String name) {
        this.name = name;
        list = new ArrayList<>();
    }
    public void addProduct(Product p){
        list.add(p);
    }
    public double averageDiscount(){
        return list.stream().mapToDouble(Product::getProcent).average().orElse(0.0);
    }

    public int sumDis(){
        int cena = list.stream().mapToInt(Product::getCena).sum();
        int popust = list.stream().mapToInt(Product::getCenapopust).sum();
        return cena - popust;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(String.format("Average discount: %.1f%%\n",averageDiscount()));
        sb.append(String.format("Total discount: %d\n",sumDis()));
        list.stream().sorted(Comparator.comparing(Product::getProcent).thenComparing(Product::getCenapopust).reversed()).forEach(p -> sb.append(p).append("\n"));
        return sb.toString();
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::print);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::print);
    }
}

// Vashiot kod ovde