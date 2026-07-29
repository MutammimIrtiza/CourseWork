package structural;

interface Coffee {
    String getDescription();
    double getCost();
}


class PlainCoffee implements Coffee {
    public String getDescription() {return "Plain Coffee";}
    public double getCost() {return 1.0;}
}


abstract class CoffeeDecorator implements Coffee {
    protected Coffee wrapee;

    public CoffeeDecorator(Coffee coffee) {
        this.wrapee = coffee;
    }
    public String getDescription() {return wrapee.getDescription();}
    public double getCost() {return wrapee.getCost();}
}


class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    public String getDescription() {return super.getDescription() + ", Milk";}
    public double getCost() {return super.getCost() + 0.25;}
}


public class decorator {
    public static void main(String[] args) {
        Coffee coffee = new PlainCoffee();
        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription());
        System.out.println(coffee.getCost());
    }
}
