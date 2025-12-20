//package discounts;
//
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//class Store{
//    private String name;
//    private List<Integer> popusti;
//    private List<Integer> ceni;
//
//    public Store(String name, List<Integer> popusti,List<Integer> ceni) {
//        this.name = name;
//        this.popusti = popusti;
//        this.ceni = ceni;
//    }
//
//    public int popust(){
//        popusti.forEach(p );
//    }
//}
//
//class Discounts{
//    List<Store> stores;
//    public Discounts(){
//        this.stores = new ArrayList<>();
//    }
//
//    public int readStores(InputStream in){
//        Scanner sc = new Scanner(in);
//        int i = 0;
//        while (sc.hasNext()){
//            String[] line = sc.nextLine().split("\\s+");
//            String name = line[0];
//            List<Integer> ceni = new ArrayList<>();
//            List<Integer>popusti = new ArrayList<>();
//            for(int i=1;i<line.length;i++){
//                int popust = Integer.parseInt(line[i].split(":")[0]);
//                int cena = Integer.parseInt(line[i].split(":")[1]);
//                popusti.add(popust);
//                ceni.add(cena);
//            }
//            stores.add(new Store(name,popusti,ceni));
//            i++;
//        }
//        return i;
//    }
//
//    public List<Store> byAverageDiscount(){
//
//    }
//}
//
//public class DiscountsTest {
//    public static void main(String[] args) {
//        Discounts discounts = new Discounts();
//        int stores = discounts.readStores(System.in);
//        System.out.println("Stores read: " + stores);
//        System.out.println("=== By average discount ===");
//        discounts.byAverageDiscount().forEach(System.out::println);
//        System.out.println("=== By total discount ===");
//        discounts.byTotalDiscount().forEach(System.out::println);
//    }
//}
//
//// Vashiot kod ovde
