// File: C1_Ramadan_Package_Decorator.java
/*
 * Pattern Chosen: Decorator[cite: 1]
 * Reason: ZBazar's core Ramadan packages (Standard, Special, Premium) must remain unmodified[cite: 1]. The Decorator 
 * pattern enables adding optional enhancements (Fruit Package, Sweet Package, Premium Packaging) in any 
 * combination at runtime while preserving the underlying package structure and calculating updated pricing[cite: 1].
 */

// Base Component
interface RamadanPackage {
    String getDescription();
    double getPrice();
}

// Concrete Components
class StandardPackage implements RamadanPackage {
    @Override
    public String getDescription() {
        return "Standard Ramadan Package";
    }

    @Override
    public double getPrice() {
        return 80.00;
    }
}

class PremiumPackage implements RamadanPackage {
    @Override
    public String getDescription() {
        return "Premium Ramadan Package";
    }

    @Override
    public double getPrice() {
        return 150.00;
    }
}

// Base Decorator
abstract class PackageDecorator implements RamadanPackage {
    protected RamadanPackage basePackage;

    public PackageDecorator(RamadanPackage basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public String getDescription() {
        return basePackage.getDescription();
    }

    @Override
    public double getPrice() {
        return basePackage.getPrice();
    }
}

// Concrete Decorators
class FruitPackageEnhancement extends PackageDecorator {
    public FruitPackageEnhancement(RamadanPackage basePackage) {
        super(basePackage);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Predefined Fruit Package";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 25.00;
    }
}

class PremiumPackagingEnhancement extends PackageDecorator {
    public PremiumPackagingEnhancement(RamadanPackage basePackage) {
        super(basePackage);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " [Wrapped in Premium Gift Packaging]";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 10.00;
    }
}

public class C1_Ramadan_Package_Decorator {
    public static void main(String[] args) {
        RamadanPackage giftPackage = new StandardPackage();
        giftPackage = new FruitPackageEnhancement(giftPackage);
        giftPackage = new PremiumPackagingEnhancement(giftPackage);

        System.out.println("Order: " + giftPackage.getDescription());
        System.out.println("Total Price: $" + giftPackage.getPrice());
    }
}