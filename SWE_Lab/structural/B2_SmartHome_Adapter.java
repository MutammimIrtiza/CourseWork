// File: B2_SmartHome_Adapter.java
/*
 * Pattern Chosen: Adapter[cite: 1]
 * Reason: Third-party smart devices (`OldSmartBulb`, `LegacyHeater`) possess incompatible method interfaces 
 * (`powerOn`, `startHeating`) and cannot be modified directly[cite: 1]. The Adapter pattern converts their 
 * interfaces to conform to the standard `SmartDevice` interface (`turnOn`, `turnOff`) expected by the app[cite: 1].
 */

// Target Interface (Given)
interface SmartDevice {
    void turnOn();
    void turnOff();
}

// Existing Standard Device
class SmartFan implements SmartDevice {
    @Override
    public void turnOn() {
        System.out.println("Smart Fan is turned ON.");
    }

    @Override
    public void turnOff() {
        System.out.println("Smart Fan is turned OFF.");
    }
}

// Adaptee 1 (Given)
class OldSmartBulb {
    public void powerOn() {
        System.out.println("Old Smart Bulb powering on...");
    }
    public void powerOff() {
        System.out.println("Old Smart Bulb powering off...");
    }
}

// Adaptee 2 (Given)
class LegacyHeater {
    public void startHeating() {
        System.out.println("Legacy Heater started heating.");
    }
    public void stopHeating() {
        System.out.println("Legacy Heater stopped heating.");
    }
}

// Adapter 1
class OldSmartBulbAdapter implements SmartDevice {
    private OldSmartBulb bulb;

    public OldSmartBulbAdapter(OldSmartBulb bulb) {
        this.bulb = bulb;
    }

    @Override
    public void turnOn() {
        bulb.powerOn();
    }

    @Override
    public void turnOff() {
        bulb.powerOff();
    }
}

// Adapter 2
class LegacyHeaterAdapter implements SmartDevice {
    private LegacyHeater heater;

    public LegacyHeaterAdapter(LegacyHeater heater) {
        this.heater = heater;
    }

    @Override
    public void turnOn() {
        heater.startHeating();
    }

    @Override
    public void turnOff() {
        heater.stopHeating();
    }
}

public class B2_SmartHome_Adapter {
    public static void main(String[] args) {
        SmartDevice fan = new SmartFan();
        SmartDevice bulbAdapter = new OldSmartBulbAdapter(new OldSmartBulb());
        SmartDevice heaterAdapter = new LegacyHeaterAdapter(new LegacyHeater());

        fan.turnOn();
        bulbAdapter.turnOn();
        heaterAdapter.turnOn();

        heaterAdapter.turnOff();
        bulbAdapter.turnOff();
    }
}