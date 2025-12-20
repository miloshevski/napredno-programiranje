package k1.cars;

import java.util.*;
import java.util.stream.Collectors;

class Car{
    private String manufacturee;
    private String model;
    private int price;
    private float power;

    public Car(String manufacturee, String model, int price, float power) {
        this.manufacturee = manufacturee;
        this.model = model;
        this.price = price;
        this.power = power;
    }

    public int getPrice() {
        return price;
    }

    public float getPower() {
        return power;
    }

    public String getManufacturee() {
        return manufacturee;
    }

    public String getModel() {
        return model;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%.0fKW) %d",manufacturee,model,power,price);
    }
}

class CarCollection{
    private List<Car> list;

    public CarCollection() {
        list = new ArrayList<>();
    }

    public void addCar(Car car){
        list.add(car);
    }

    public void sortByPrice(boolean ascending){
        if(ascending){
            list = list.stream().sorted(Comparator.comparingInt(Car::getPrice).thenComparing(Car::getPower)).collect(Collectors.toList());
            return;
        }
        list = list.stream().sorted(Comparator.comparingInt(Car::getPrice).thenComparing(Car::getPower).reversed()).collect(Collectors.toList());

    }
    public List<Car> filterByManufacturer(String manufacturer){
        return list.stream().filter(c -> c.getManufacturee().toLowerCase().equals(manufacturer.toLowerCase())).sorted(Comparator.comparing(Car::getModel)).collect(Collectors.toList());
    }
    public List<Car> getList(){
        return list;
    }
}

public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if(parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}


// vashiot kod ovdet kod ovde