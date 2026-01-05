package products;

import java.util.ArrayList;
import java.util.List;

// COMPONENT
interface Item {
    double getPrice();
    void print(String indent);
}

// LEAF
class Product implements Item {
    private final String name;
    private final double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void print(String indent) {
        System.out.printf("%s- Product: %s (%.2f)%n", indent, name, price);
    }
}

// COMPOSITE
class Box implements Item {
    private final String name;
    private final List<Item> items = new ArrayList<>();

    public Box(String name) {
        this.name = name;
    }

    public void add(Item item) {
        items.add(item);
    }

    public void remove(Item item) {
        items.remove(item);
    }

    @Override
    public double getPrice() {
        double total = 0.0;
        for (Item item : items) {
            total += item.getPrice();
        }
        return total;
    }

    @Override
    public void print(String indent) {
        System.out.printf("%s+ Box: %s (total %.2f)%n", indent, name, getPrice());
        for (Item item : items) {
            item.print(indent + "  ");
        }
    }
}

// CLIENT / DEMO
public class CompositeProductsBoxesDemo {
    public static void main(String[] args) {
        // Leaf products
        Item phone = new Product("Phone", 699.99);
        Item charger = new Product("Charger", 19.99);
        Item headphones = new Product("Headphones", 49.99);
        Item laptop = new Product("Laptop", 1199.00);

        // Small box with accessories
        Box accessoriesBox = new Box("Accessories Box");
        accessoriesBox.add(charger);
        accessoriesBox.add(headphones);

        // Bigger box that can contain products + other boxes
        Box electronicsBox = new Box("Electronics Box");
        electronicsBox.add(phone);
        electronicsBox.add(accessoriesBox); // nested box
        electronicsBox.add(laptop);

        Box bigBox = new Box("Big box");
        bigBox.add(phone);
        bigBox.add(charger);
        bigBox.add(headphones);
        bigBox.add(laptop);
        bigBox.add(accessoriesBox);
        bigBox.add(electronicsBox);

        // Print structure + total
        bigBox.print("");
        System.out.printf("%nGRAND TOTAL = %.2f%n", bigBox.getPrice());
    }
}
