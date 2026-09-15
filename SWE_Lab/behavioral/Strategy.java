
interface PaymentStrategy {
    void pay(Double amount);
}

class CardPayment implements PaymentStrategy {
    private String cardNumber;
    
    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(Double amount) {
        System.out.println("Paying via card : " + this.cardNumber);    
    }
    
}

class BkashPayment implements PaymentStrategy {

    @Override
    public void pay(Double amount) {
        // TODO Auto-generated method stub
        
    }
    
}

class ShoppingCart {
    PaymentStrategy strategy;
    void setStrategy(PaymentStrategy s) {strategy = s;}

    public void pay(Double amount) {strategy.pay(amount);}

}

public class Strategy {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.setStrategy(new CardPayment("342LLKJ4"));
        cart.pay(23.232);
    }
}
