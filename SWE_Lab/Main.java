
interface Item {
    String getDesc();
    double getPrice();
}

abstract class ItemDecorator implements Item {
    protected Item item;

    public ItemDecorator(Item item) {
        this.item = item;
    }
    
}

class ItemWrapper extends ItemDecorator {
    public ItemWrapper(Item item) {
        super(item);
    }

    public double getPrice() {
        return item.getPrice() + 2.0;
    }
    public String getDesc() {
        return item.getDesc() + " [Wrapped]";
    }
}

abstract class ItemDelivery extends ItemDecorator {
    protected double miles;
    public ItemDelivery(Item item, double miles) {
        super(item);
        this.miles = miles;
    }
}

class LocalDelivery extends ItemDelivery {
    public LocalDelivery(Item item, double miles) {
        super(item, miles);
    }

    @Override
    public String getDesc() {
        return item.getDesc() + "[Local Delivery]";
    }

    @Override
    public double getPrice() {
        return item.getPrice() + 1.0 * miles;
    }
}

abstract class DeliveryWithMode {
    ItemDelivery delivery;

    public DeliveryWithMode(ItemDelivery delivery) {
        this.delivery = delivery;
    }
    abstract String getDesc();
    abstract double getPrice();
}

class ExpressDelivery extends DeliveryWithMode {
    
}

public class Main {
    public static void main(String[] args) {
        
    }
}