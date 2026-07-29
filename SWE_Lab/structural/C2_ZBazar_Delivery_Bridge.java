// File: C2_ZBazar_Delivery_Bridge.java
/*
 * Pattern Chosen: Bridge[cite: 1]
 * Reason: Delivery options (Standard, Express, Scheduled) and physical transportation mechanisms (Bike, Van, Drone, Robot) 
 * represent two distinct, changing dimensions[cite: 1]. The Bridge pattern decouples delivery business logic from transport 
 * implementation, allowing new transport methods like drones to be integrated without altering delivery options[cite: 1].
 */

// Implementor: Physical Transport Method
interface TransportMethod {
    void dispatchParcel(String destination);
}

// Concrete Implementors
class BikeCourier implements TransportMethod {
    @Override
    public void dispatchParcel(String destination) {
        System.out.println("Dispatching via Bike Courier to " + destination);
    }
}

class DroneTransport implements TransportMethod {
    @Override
    public void dispatchParcel(String destination) {
        System.out.println("Dispatching via Drone to " + destination + " (Performing pre-flight safety check)");
    }
}

// Abstraction: Delivery Option
abstract class DeliveryType {
    protected TransportMethod transport;

    public DeliveryType(TransportMethod transport) {
        this.transport = transport;
    }

    public abstract void processDelivery(String destination);
}

// Refined Abstractions
class ExpressDelivery extends DeliveryType {
    public ExpressDelivery(TransportMethod transport) {
        super(transport);
    }

    @Override
    public void processDelivery(String destination) {
        System.out.println("[Express Delivery - 4 Hours]");
        transport.dispatchParcel(destination);
    }
}

class ScheduledDelivery extends DeliveryType {
    private String timeSlot;

    public ScheduledDelivery(TransportMethod transport, String timeSlot) {
        super(transport);
        this.timeSlot = timeSlot;
    }

    @Override
    public void processDelivery(String destination) {
        System.out.println("[Scheduled Delivery - Slot: " + timeSlot + "]");
        transport.dispatchParcel(destination);
    }
}

public class C2_ZBazar_Delivery_Bridge {
    public static void main(String[] args) {
        TransportMethod drone = new DroneTransport();
        TransportMethod bike = new BikeCourier();

        DeliveryType expressOrder = new ExpressDelivery(drone);
        DeliveryType scheduledOrder = new ScheduledDelivery(bike, "Tomorrow 10:00 AM");

        expressOrder.processDelivery("Sector 7, Uttara");
        System.out.println("---");
        scheduledOrder.processDelivery("Dhanmondi Lake");
    }
}