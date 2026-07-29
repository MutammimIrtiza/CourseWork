// File: A2_ZBazar_Custom_Bazar.java
/*
 * Pattern Chosen: Composite[cite: 1]
 * Reason: Customers can create custom packages by combining single grocery items, preset packages, and even 
 * previously created custom packages[cite: 1]. The Composite pattern allows treating individual items (leaf nodes) 
 * and package structures (composite nodes) uniformly when calculating total price, weight, and displaying hierarchy[cite: 1].
 */

import java.util.ArrayList;
import java.util.List;

// Component
interface BazarComponent {
    double getPrice();
    double getWeight();
    void displayStructure(int indent);
}

// Leaf
class SingleItem implements BazarComponent {
    private String name;
    private double price;
    private double weight;

    public SingleItem(String name, double price, double weight) {
        this.name = name;
        this.price = price;
        this.weight = weight;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void displayStructure(int indent) {
        String indentation = " ".repeat(indent);
        System.out.println(indentation + "- " + name + " (Price: $" + price + ", Weight: " + weight + "kg)");
    }
}

// Composite
class BazarPackage implements BazarComponent {
    private String packageName;
    private List<BazarComponent> components = new ArrayList<>();

    public BazarPackage(String packageName) {
        this.packageName = packageName;
    }

    public void add(BazarComponent component) {
        components.add(component);
    }

    public void remove(BazarComponent component) {
        components.remove(component);
    }

    @Override
    public double getPrice() {
        double total = 0;
        for (BazarComponent component : components) {
            total += component.getPrice();
        }
        return total;
    }

    @Override
    public double getWeight() {
        double total = 0;
        for (BazarComponent component : components) {
            total += component.getWeight();
        }
        return total;
    }

    @Override
    public void displayStructure(int indent) {
        String indentation = " ".repeat(indent);
        System.out.println(indentation + "+ Package: " + packageName + 
                           " [Total Price: $" + getPrice() + ", Total Weight: " + getWeight() + "kg]");
        for (BazarComponent component : components) {
            component.displayStructure(indent + 4);
        }
    }
}

public class A2_ZBazar_Custom_Bazar {
    public static void main(String[] args) {
        SingleItem rice = new SingleItem("Rice", 15.0, 5.0);
        SingleItem oil = new SingleItem("Oil", 8.0, 2.0);

        BazarPackage smallBundle = new BazarPackage("Small Preset Bundle");
        smallBundle.add(rice);
        smallBundle.add(oil);

        SingleItem pulse = new SingleItem("Pulse", 5.0, 1.0);

        BazarPackage customBazar = new BazarPackage("My Custom Bazar");
        customBazar.add(smallBundle);
        customBazar.add(pulse);

        customBazar.displayStructure(0);
    }
}