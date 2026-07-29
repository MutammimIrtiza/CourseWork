import java.util.*;

interface SmartDevice {
    void activate();
    void deactivate();
    double getPowerUsage();
    String getStatus();
    Class<?> getBaseType();
}

class SmartLight implements SmartDevice {
    private boolean on = false;

    @Override
    public void activate() {
        on = true;
    }
    @Override
    public void deactivate() {
        on = false;
    }
    @Override
    public double getPowerUsage() {
        return on ? 10.0 : 0.0;
    }
    @Override
    public String getStatus() {
        return "Light? " + (on ? "ON" : "OFF");
    }
    @Override
    public Class<?> getBaseType() {
         return SmartLight.class;
    }
}

class SmartThermostat implements SmartDevice {
    private boolean on = false;

    @Override
    public void activate() {
        on = true;
    }
    @Override
    public void deactivate() {
        on = false;
    }
    @Override
    public double getPowerUsage() {
        return on ? 150.0 : 0.0;
    }
    @Override
    public String getStatus() {
        return "Thermostat: " + (on ? "ON" : "OFF");
    }
    @Override
    public Class<?> getBaseType() {
         return SmartThermostat.class;
    }
}

class SmartSpeaker implements SmartDevice {
    private boolean on = false;

    @Override
    public void activate() {
        on = true;
    }
    @Override
    public void deactivate() {
        on = false;
    }
    @Override
    public double getPowerUsage() {
        return on ? 5.0 : 0.0;
    }
    @Override
    public String getStatus() {
        return "Speaker: " + (on ? "ON" : "OFF");
    }
    @Override
    public Class<?> getBaseType() {
         return SmartSpeaker.class;
    }
}

abstract class DeviceDecorator implements SmartDevice {
    protected SmartDevice device;

    public DeviceDecorator(SmartDevice device) {
        this.device = device;
    }
/*
    Delegate methods. Needed here.
    A concrete decorator will override the relevant methods only.
    So, other methods will have default behaviour.
*/
    public void activate() {
        device.activate();
    }

    public void deactivate() {
        device.deactivate();
    }

    public double getPowerUsage() {
        return device.getPowerUsage();
    }

    public String getStatus() {
        return device.getStatus();
    }

    public Class<?> getBaseType() {
        return device.getBaseType();
    }
    
}


class AccessRestricted extends DeviceDecorator {
    private int pin;
    private boolean locked = true;

    public AccessRestricted(SmartDevice device, int pin) {
        super(device);
        this.pin = pin;
    }

    public void lock () {
        this.locked = true;
    }

    public void unlock (int pin) {
        if(this.pin == pin) locked = false;
    }

    @Override
    public void activate() {
        if(!locked) super.activate();
    }

    @Override
    public void deactivate() {
        if(!locked) super.deactivate();
    }

    @Override
    public String getStatus() {
        return super.getStatus() + (locked ? " [LOCKED]" : "");
    }
}


class TimerControlled extends DeviceDecorator {
    private int duration;
    private boolean tiemrOn = false;

    public TimerControlled(SmartDevice device, int duration) {
        super(device);
        this.duration = duration;
    }

    @Override
    public void activate() {
        super.activate();
        tiemrOn = true;
    }

    @Override
    public void deactivate() {
        super.deactivate();
        tiemrOn = false;
    }

    void simulateTimerExpiry() {
        if(tiemrOn) {
            super.deactivate();
            tiemrOn = false;
        }
    }

    @Override
    public String getStatus() {
        return super.getStatus() + (tiemrOn ? " (auto-off in " + duration + "s)" : "");
    }

}


class PowerThrottled extends DeviceDecorator {
    private double powerCap;

    public PowerThrottled(SmartDevice device, double powerCap) {
        super(device);
        this.powerCap = powerCap;
    }

    @Override
    public double getPowerUsage() {
        double p = super.getPowerUsage(); 
        return Math.min(p, powerCap);
    }

    @Override
    public String getStatus() {
        return super.getStatus() + 
        (super.getPowerUsage() > powerCap ? 
            " [throttled to " + powerCap + "W]"  : "");
    }
}

/*
    Not given as a type, but this is useful for guest mode of room/home
*/
class GuestRestrictedDevice extends DeviceDecorator {
    public GuestRestrictedDevice(SmartDevice device) { 
        super(device); 
    }

    @Override 
    public void activate() {
        // do nothing
    }

    @Override 
    public double getPowerUsage() { 
        return 0.0; 
    }

    @Override public String getStatus() { 
        return super.getStatus() + " [guest-restricted]"; 
    }
}



/*
    making a deviceContainer interface because
    eco mode and guest mode should work on room & home only.

    the getters are need so that the decorators can access the variables.
*/
interface DeviceContainer extends SmartDevice {
    List<SmartDevice> getDevices();
    String getName();
    String getMode();
}

class Room implements DeviceContainer {
    List<SmartDevice> devices = new ArrayList<>();
    String name;
    
    public Room(String name) {
        this.name = name;
    }

    void addDevice(SmartDevice d) {
        devices.add(d);
    }

    @Override
    public List<SmartDevice> getDevices() {
        return devices;
    }

    @Override
    public String getName() {
        return "[" + name + "]";
    }

    @Override
    public void activate() {
        for(SmartDevice d : devices) d.activate();
    }

    @Override
    public void deactivate() {
        for(SmartDevice d : devices) d.deactivate();
    }

    @Override
    public double getPowerUsage() {
        double total = 0;
        for(SmartDevice d : devices) total += d.getPowerUsage();
        return total;
    }

    @Override
    public String getMode() {
        return null;
    }

    @Override
    public String getStatus() {
        StringBuilder s = new StringBuilder();
        s.append(getName());
        for(SmartDevice d : devices) {
            s.append("\n").append(d.getStatus());
        }
        return s.toString();
    }
    @Override
    public Class<?> getBaseType() {
         return Room.class;
    }

}

class Home implements DeviceContainer {
    List<SmartDevice> rooms = new ArrayList<>();
    String name;
    
    public Home(String name) {
        this.name = name;
    }

    void addRoom(SmartDevice r) {
        rooms.add(r);
    }

    @Override
    public List<SmartDevice> getDevices() {
        return rooms;
    }

    @Override
    public String getName() {
        return "[" + name + "]";
    }

    @Override
    public void activate() {
        for(SmartDevice r : rooms) r.activate();
    }

    @Override
    public void deactivate() {
        for(SmartDevice r : rooms) r.deactivate();
    }

    @Override
    public double getPowerUsage() {
        double total = 0;
        for(SmartDevice r : rooms) total += r.getPowerUsage();
        return total;
    }

    @Override
    public String getMode() {
        return null;
    }

    @Override
    public String getStatus() {
        StringBuilder s = new StringBuilder();
        s.append(getName());
        for(SmartDevice r : rooms) {
            s.append("\n").append(r.getStatus());
        }
        return s.toString();
    }
    @Override
    public Class<?> getBaseType() {
         return Home.class;
    }
}


abstract class ContainerDecorator implements DeviceContainer {
    DeviceContainer container;

    public ContainerDecorator(DeviceContainer container) {
        this.container = container;
    }

    public void activate() {
        container.activate();
    }

    public void deactivate() {
        container.deactivate();
    }

    public double getPowerUsage() {
        return container.getPowerUsage();
    }

    public String getStatus() {
        return container.getStatus();
    }

    public List<SmartDevice> getDevices() {
        return container.getDevices();
    }

    public String getName() {
        return container.getName();
    }

    public String getMode() {
        return container.getMode();
    }

    public Class<?> getBaseType() {
        return container.getBaseType();
    }

}

class EcoMode extends ContainerDecorator {
    private double powerBudget;

    public EcoMode(DeviceContainer container, double powerBudget) {
        super(container);
        this.powerBudget = powerBudget;
    }

    @Override
    public void activate() {
        super.activate();
        List<SmartDevice> devices = super.getDevices();
        for(int i = devices.size() - 1; i >= 0; i--) {
            if(super.getPowerUsage() <= powerBudget) break;
            devices.get(i).deactivate();
        }
    }

    @Override
    public String getStatus() {
        StringBuilder s = new StringBuilder();
        s.append(super.getName());
        if(super.getMode() != null) s.append(super.getMode());
        s.append(getMode() + "\n");
        for(SmartDevice d : super.getDevices()) {
            s.append("\n").append(d.getStatus());
        }
        return s.toString();
    }

    @Override
    public String getMode() {
        return " [ECO: " + powerBudget + "W budget]";
    }
    
}

class GuestMode extends ContainerDecorator {
    private List<SmartDevice> guestDevices = new ArrayList<>();
    
    public GuestMode(DeviceContainer container, Set<Class<?>> allowed) {
        super(container);
        this.guestDevices = new ArrayList<>();
        
        for (SmartDevice d : super.getDevices()) {
            if (d instanceof DeviceContainer) {
                this.guestDevices.add(new GuestMode((DeviceContainer) d, allowed));
            } else if (allowed.contains(d.getBaseType())) {
                this.guestDevices.add(d);
            } else {
                this.guestDevices.add(new GuestRestrictedDevice(d));
            }
        }
    }

    public void activate() {
        for (SmartDevice d : guestDevices) d.activate();
    }

    public void deactivate() {
        for (SmartDevice d : guestDevices) d.deactivate();
    }

    public double getPowerUsage() {
        double total = 0;
        for (SmartDevice d : guestDevices) total += d.getPowerUsage();
        return total;
    }

    @Override
    public String getStatus() {
        StringBuilder s = new StringBuilder();
        s.append(super.getName() + super.getMode());
        s.append(getMode() + "\n");
        for(SmartDevice d : guestDevices) {
            s.append("\n").append(d.getStatus());
        }
        return s.toString();
    }

    @Override
    public String getMode() {
        return "[GUEST MODE]";
    }

}

