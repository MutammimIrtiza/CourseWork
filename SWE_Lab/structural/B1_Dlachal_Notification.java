// File: B1_Dlachal_Notification.java
/*
 * Pattern Chosen: Bridge[cite: 1]
 * Reason: The notification system evolves along two independent dimensions: communication channels (SMS, WhatsApp, Push) 
 * and notification event types (Payment Failed, Order Dispatched)[cite: 1]. The Bridge pattern decouples the 
 * abstraction (event business logic) from its implementation (delivery channels), avoiding class multiplication[cite: 1].
 */

// Implementor
interface CommunicationChannel {
    void deliverMessage(String title, String body);
}

// Concrete Implementors
class SMSChannel implements CommunicationChannel {
    @Override
    public void deliverMessage(String title, String body) {
        System.out.println("[SMS] " + title + " - " + body);
    }
}

class WhatsAppChannel implements CommunicationChannel {
    @Override
    public void deliverMessage(String title, String body) {
        System.out.println("[WhatsApp] " + title + ": " + body);
    }
}

// Abstraction
abstract class NotificationEvent {
    protected CommunicationChannel channel;

    public NotificationEvent(CommunicationChannel channel) {
        this.channel = channel;
    }

    public abstract void notifyUser();
}

// Refined Abstractions
class PaymentFailedEvent extends NotificationEvent {
    public PaymentFailedEvent(CommunicationChannel channel) {
        super(channel);
    }

    @Override
    public void notifyUser() {
        channel.deliverMessage("Action Required: Payment Failed", "Your recent transaction failed. Please update payment method.");
    }
}

class DispatchEvent extends NotificationEvent {
    public DispatchEvent(CommunicationChannel channel) {
        super(channel);
    }

    @Override
    public void notifyUser() {
        channel.deliverMessage("Great News: Bazar Dispatched", "Your monthly bazar is on its way!");
    }
}

public class B1_Dlachal_Notification {
    public static void main(String[] args) {
        CommunicationChannel sms = new SMSChannel();
        CommunicationChannel whatsapp = new WhatsAppChannel();

        NotificationEvent alert1 = new PaymentFailedEvent(sms);
        NotificationEvent alert2 = new DispatchEvent(whatsapp);

        alert1.notifyUser();
        alert2.notifyUser();
    }
}