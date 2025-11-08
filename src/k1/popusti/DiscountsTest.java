package k1.popusti;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class Product implements Comparable<Product> {
    private final int discountedPrice;
    private final int price;

    public Product(int discountedPrice, int price) {
        this.discountedPrice = discountedPrice;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", getDiscountRelative(), getDiscountedPrice(), getPrice());
    }

    @Override
    public int compareTo(Product o) {
        return Comparator
                .comparing(Product::getDiscountRelative)
                .thenComparing(Product::getDiscountAbsolute)
                .reversed()
                .compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return discountedPrice == product.discountedPrice && price == product.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountedPrice, price);
    }

    public int getDiscountRelative() {
        return 100 - (discountedPrice * 100 / price);
    }

    public int getDiscountAbsolute() {
        return price - discountedPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public int getPrice() {
        return price;
    }
}

class Store {
    private final String name;
    private final List<Product> products;

    public Store(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    @Override
    public String toString() {
        return String.format("%s%nAverage discount: %.1f%%%nTotal discount: %d%n%s",
                name,
                getAverageDiscount(),
                getTotalDiscount(),
                products
                        .stream()
                        .sorted()
                        .map(Product::toString)
                        .collect(Collectors.joining("\n")));
    }

    public double getAverageDiscount() {
        return products
                .stream()
                .mapToDouble(Product::getDiscountRelative)
                .average()
                .orElse(0);
    }

    public int getTotalDiscount() {
        return products
                .stream()
                .mapToInt(Product::getDiscountAbsolute)
                .sum();
    }

    public String getName() {
        return name;
    }
}

class Discounts {
    private final List<Store> stores;

    public Discounts() {
        this.stores = new ArrayList<>();
    }

    public int readStores(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        br.lines().forEach(line -> {
            String[] tokens = line.split("\\s+");
            List<Product> products = new ArrayList<>();

            for (int i = 1; i < tokens.length; i++) {
                String[] prices = tokens[i].split(":");
                products.add(new Product(Integer.parseInt(prices[0]), Integer.parseInt(prices[1])));
            }

            stores.add(new Store(tokens[0], products));
        });

        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores
                .stream()
                .sorted(Comparator
                        .comparing(Store::getAverageDiscount)
                        .reversed()
                        .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores
                .stream()
                .sorted(Comparator
                        .comparing(Store::getTotalDiscount)
                        .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
}
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde
