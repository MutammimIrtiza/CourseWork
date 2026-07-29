

// ==========================================
// 1. ABSTRACT PRODUCTS (Component Interfaces)
// ==========================================
interface Processor {
    String getSpecs();
}

interface Display {
    String getSpecs();
}

// ==========================================
// 2. CONCRETE PRODUCTS (Specific Components)
// ==========================================
class IntelXeonProcessor implements Processor {
    public String getSpecs() { return "Intel Xeon (High-Performance Workstation CPU)"; }
}

class ARMProcessor implements Processor {
    public String getSpecs() { return "ARM Processor (Power-Efficient & Lightweight CPU)"; }
}

class IPSDisplay implements Display {
    public String getSpecs() { return "IPS Display (Accurate Colors & Wide Viewing Angles)"; }
}

class OLEDDisplay implements Display {
    public String getSpecs() { return "OLED Display (Deep Blacks & Ultra-Thin)"; }
}

// ==========================================
// 3. ABSTRACT FACTORY
// ==========================================
interface ComputerComponentFactory {
    Processor createProcessor();
    Display createDisplay();
}

// ==========================================
// 4. CONCRETE FACTORIES
// ==========================================
class WorkProComponentFactory implements ComputerComponentFactory {
    public Processor createProcessor() { return new IntelXeonProcessor(); }
    public Display createDisplay() { return new IPSDisplay(); }
}

class LiteMaxComponentFactory implements ComputerComponentFactory {
    public Processor createProcessor() { return new ARMProcessor(); }
    public Display createDisplay() { return new OLEDDisplay(); }
}

// ==========================================
// 5. CLIENT CLASSES (The Computers)
// ==========================================
abstract class Computer {
    protected String modelName;
    protected Processor processor;
    protected Display display;
    protected String characteristics;

    // The computer is composed using the abstract factory components
    public abstract void assemble(ComputerComponentFactory factory);

    public void printDescription() {
        System.out.println("=========================================");
        System.out.println("Model Name:      " + modelName);
        System.out.println("Processor:       " + processor.getSpecs());
        System.out.println("Display Type:    " + display.getSpecs());
        System.out.println("Characteristics: " + characteristics);
        System.out.println("=========================================\n");
    }
}

class WorkPro extends Computer {
    public void assemble(ComputerComponentFactory factory) {
        this.modelName = "WorkPro";
        this.characteristics = "Professional Heavy-Duty Workstation optimized for intense processing tasks.";
        this.processor = factory.createProcessor();
        this.display = factory.createDisplay();
    }
}

class LiteMax extends Computer {
    public void assemble(ComputerComponentFactory factory) {
        this.modelName = "LiteMax";
        this.characteristics = "Ultra-lightweight everyday device optimized for long battery life and portability.";
        this.processor = factory.createProcessor();
        this.display = factory.createDisplay();
    }
}

// ==========================================
// 6. DEMONSTRATION (User Selection Simulation)
// ==========================================
public class Main {
    public static void main(String[] args) {
        // Simulating User Selection 1: User wants a WorkPro
        String userChoice1 = "WorkPro";
        Computer myComputer1 = null;
        ComputerComponentFactory factory1 = null;

        if (userChoice1.equalsIgnoreCase("WorkPro")) {
            myComputer1 = new WorkPro();
            factory1 = new WorkProComponentFactory();
        }
        
        if (myComputer1 != null) {
            myComputer1.assemble(factory1);
            myComputer1.printDescription();
        }

        // Simulating User Selection 2: User wants a LiteMax
        String userChoice2 = "LiteMax";
        Computer myComputer2 = null;
        ComputerComponentFactory factory2 = null;

        if (userChoice2.equalsIgnoreCase("LiteMax")) {
            myComputer2 = new LiteMax();
            factory2 = new LiteMaxComponentFactory();
        }

        if (myComputer2 != null) {
            myComputer2.assemble(factory2);
            myComputer2.printDescription();
        }
    }
}