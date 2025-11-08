package k1.ddv;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.PublicKey;
import java.util.*;

enum TYPE{
    A,B,C
}

class Item{
    private int price;
    private TYPE type;
    private double ddv;
    private double price_with_ddv;

    public Item(int price, String t) {
        this.price = price;
        this.type = parseType(t);
        this.ddv = type == TYPE.A ? 0.18 : type == TYPE.B ? 0.05 : 0;
        this.price_with_ddv = price + price * ddv;
    }
    public double getPovratok(){
        return (price * ddv) * 0.15;
    }
    public static TYPE parseType(String t){
        return t.equals("A") ? TYPE.A : t.equals("B") ? TYPE.B : TYPE.C;
    }

    public int getPrice() {
        return price;
    }

    public TYPE getType() {
        return type;
    }

    public double getDdv() {
        return ddv;
    }

    public double getPrice_with_ddv() {
        return price_with_ddv;
    }
}

class Smetka{
    private final int id;
    private List<Item> list;

    public Smetka(int id, List<Item> list) {
        this.id = id;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public List<Item> getList() {
        return list;
    }

    public int sumAmount(){
        return list.stream().mapToInt(Item::getPrice).sum();
    }

    public double taxReturn(){
        return list.stream().mapToDouble(Item::getPovratok).sum();
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f", id, sumAmount(), taxReturn());
    }
}

class MojDDV{
    private List<Smetka> smetki;

    public MojDDV(){
        this.smetki = new ArrayList<>();
    }

    public static TYPE parseType(String t){
        return t.equals("A") ? TYPE.A : t.equals("B") ? TYPE.B : TYPE.C;
    }

    public void readRecords(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.trim().isEmpty()) continue;

            String[] s = line.split("\\s+");
            int id = Integer.parseInt(s[0]);

            List<Item> items = new ArrayList<>();
            for (int i = 1; i < s.length; i += 2) {
                int amount = Integer.parseInt(s[i]);
                String type = s[i + 1];
                items.add(new Item(amount, type));
            }

            Smetka smetka = new Smetka(id, items);
            int sum = smetka.sumAmount();

            try {
                if (sum > 30000) {
                    throw new AmountNotAllowedException(sum);
                }
                smetki.add(smetka);  // store only valid receipts
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());  // print error immediately
            }
        }

    }


    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        for (Smetka s : smetki) {
            pw.println(s.toString());
        }

        pw.flush();
    }

    public void printStatistics(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        DoubleSummaryStatistics dss = smetki.stream().filter(Objects::nonNull).mapToDouble(Smetka::taxReturn).summaryStatistics();
        pw.printf("min:\t%5.3f%nmax:\t%5.3f%nsum:\t%5.3f%ncount:\t%-5d%navg:\t%5.3f%n", dss.getMin(), dss.getMax(), dss.getSum(), dss.getCount(), dss.getAverage());
        pw.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int s) {
        super(String.format("Receipt with amount %d is not allowed to be scanned",s));
    }
}