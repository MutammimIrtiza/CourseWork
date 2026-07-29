package structural;

// =====================================================================
// 1. IMPLEMENTOR INTERFACE
// Low-level color operations.
// =====================================================================
interface Color {
    void applyColor();
}

// =====================================================================
// 2. CONCRETE IMPLEMENTORS
// Platform/rendering specific details for colors.
// =====================================================================
class RedColor implements Color {
    @Override
    public void applyColor() {
        System.out.print("red");
    }
}

class BlueColor implements Color {
    @Override
    public void applyColor() {
        System.out.print("blue");
    }
}

// =====================================================================
// 3. ABSTRACTION
// Maintains a reference to the Color implementor (the Bridge).
// =====================================================================
abstract class Shape {
    protected Color color; // The Bridge

    public Shape(Color color) {
        this.color = color;
    }

    public abstract void draw();
}

// =====================================================================
// 4. REFINED ABSTRACTIONS
// High-level shape implementations.
// =====================================================================
class Circle extends Shape {
    private final int radius;

    public Circle(int radius, Color color) {
        super(color);
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.print("Drawing a ");
        color.applyColor(); // Delegates color rendering to the bridge reference
        System.out.println(" circle with radius " + radius + "px.");
    }
}

class Square extends Shape {
    private final int sideLength;

    public Square(int sideLength, Color color) {
        super(color);
        this.sideLength = sideLength;
    }

    @Override
    public void draw() {
        System.out.print("Drawing a ");
        color.applyColor(); // Delegates color rendering to the bridge reference
        System.out.println(" square with side " + sideLength + "px.");
    }
}

// =====================================================================
// 5. CLIENT CODE
// =====================================================================
public class Bridge {
    public static void main(String[] args) {
        // Instantiate implementors
        Color red = new RedColor();
        Color blue = new BlueColor();

        // Pass implementors directly into abstractions at runtime
        Shape redCircle = new Circle(10, red);
        Shape blueCircle = new Circle(15, blue);

        Shape redSquare = new Square(20, red);
        Shape blueSquare = new Square(25, blue);

        System.out.println("--- Rendering Shapes ---");
        redCircle.draw();   // Drawing a red circle with radius 10px.
        blueCircle.draw();  // Drawing a blue circle with radius 15px.
        redSquare.draw();   // Drawing a red square with side 20px.
        blueSquare.draw();  // Drawing a blue square with side 25px.
    }
}