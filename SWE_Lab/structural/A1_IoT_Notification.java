// File: A1_IoT_Notification.java
/*
 * Pattern Chosen: Decorator[cite: 1]
 * Reason: The system requires dynamically adding optional behaviors (Encryption, Priority Label, Logging)[cite: 1]
 * to basic notification channels (Email, SMS, Push)[cite: 1]. The Decorator pattern allows wrapping base 
 * notifications with these enhancements in any combination without modifying base classes or causing an 
 * explosion of subclasses[cite: 1].
 */

interface Notification {
    void send(String message);
}

// Concrete Components
class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}

class SMSNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

class PushNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Sending Push Notification: " + message);
    }
}

// Base Decorator
abstract class NotificationDecorator implements Notification {
    protected Notification wrappee;

    public NotificationDecorator(Notification notification) {
        this.wrappee = notification;
    }

    @Override
    public void send(String message) {
        wrappee.send(message);
    }
}

// Concrete Decorators
class EncryptionDecorator extends NotificationDecorator {
    public EncryptionDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send(String message) {
        String encryptedMessage = "[Encrypted] " + message;
        super.send(encryptedMessage);
    }
}

class PriorityDecorator extends NotificationDecorator {
    public PriorityDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send(String message) {
        String priorityMessage = "[HIGH PRIORITY] " + message;
        super.send(priorityMessage);
    }
}

class LoggingDecorator extends NotificationDecorator {
    public LoggingDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send(String message) {
        super.send(message);
        System.out.println("Log: Message delivery recorded in IoT device.");
    }
}

public class A1_IoT_Notification {
    public static void main(String[] args) {
        // Constructing a notification with Encryption, Priority, and Logging
        Notification alert = new EmailNotification();
        alert = new EncryptionDecorator(alert);
        alert = new PriorityDecorator(alert);
        alert = new LoggingDecorator(alert);

        alert.send("Motion detected at front door!");
    }
}