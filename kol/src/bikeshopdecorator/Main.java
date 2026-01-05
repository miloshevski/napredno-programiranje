package bikeshopdecorator;

interface Bike{
    double getPrice();
    String description();
}


class RoadBike implements Bike{
    private final double price;
    private final String description;

    RoadBike(double price, String description) {
        this.price = price;
        this.description = description;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String description() {
        return description;
    }

}

abstract class BaseDecorator implements Bike{
    protected Bike decoratedBike;

    public BaseDecorator(Bike decoratedBike) {
        this.decoratedBike = decoratedBike;
    }

    @Override
    public double getPrice() {
        return decoratedBike.getPrice();
    }

    @Override
    public String description() {
        return decoratedBike.description();
    }
}

class WheelsDecorator extends BaseDecorator{

    public WheelsDecorator(Bike decoratedBike) {
        super(decoratedBike);
    }

    @Override
    public double getPrice() {
        return decoratedBike.getPrice() + 100;
    }

    @Override
    public String description() {
        return decoratedBike.description() + ", new wheels";
    }


}

class HandleBars extends BaseDecorator{

    public HandleBars(Bike decoratedBike) {
        super(decoratedBike);
    }

    @Override
    public double getPrice() {
        return decoratedBike.getPrice() + 50;
    }

    @Override
    public String description() {
        return decoratedBike.description() + ", bars";
    }
}
class Garmin extends BaseDecorator{

    public Garmin(Bike decoratedBike) {
        super(decoratedBike);
    }

    @Override
    public double getPrice() {
        return decoratedBike.getPrice() + 10;
    }

    @Override
    public String description() {
        return decoratedBike.description() + ", new garmin";
    }
}

public class Main {
    static void main() {
        Bike bike = new RoadBike(1000,"Road Bike");
        System.out.println(bike.description() + " " + bike.getPrice());

        bike = new WheelsDecorator(bike);
        System.out.println(bike.description() + " " + bike.getPrice());

        bike = new HandleBars(bike);
        System.out.println(bike.description() + " " + bike.getPrice());


    }
}
