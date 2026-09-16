import java.util.Arrays;
import java.util.List;

// --- Enums ---
enum CustomerCategory {
    REGULAR,
    PREMIUM
}

enum PaymentMethod {
    CARD,
    MFS,
    CASH
}

// --- Request Object ---
class PurchaseRequest {
    private final double amount;
    private final CustomerCategory customerCategory;
    private final PaymentMethod paymentMethod;

    public PurchaseRequest(double amount, CustomerCategory customerCategory, PaymentMethod paymentMethod) {
        this.amount = amount;
        this.customerCategory = customerCategory;
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public CustomerCategory getCustomerCategory() {
        return customerCategory;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
}

// --- Strategy Interface ---
interface DiscountStrategy {
    double calculateDiscountPercentage(PurchaseRequest request);
}

// --- Concrete Strategies ---

// 1. Purchase Amount Discount Strategy (5% per complete 1,000 up to 25%)
class PurchaseAmountDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscountPercentage(PurchaseRequest request) {
        double amount = request.getAmount();
        int completeThousands = (int) (amount / 1000);
        double discount = completeThousands * 5.0;
        return Math.min(discount, 25.0);
    }
}

// 2. Customer Category Discount Strategy
class CustomerCategoryDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscountPercentage(PurchaseRequest request) {
        switch (request.getCustomerCategory()) {
            case REGULAR:
                return 5.0;
            case PREMIUM:
                return 15.0;
            default:
                return 0.0;
        }
    }
}

// 3. Payment Method Discount Strategy
class PaymentMethodDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscountPercentage(PurchaseRequest request) {
        switch (request.getPaymentMethod()) {
            case CARD:
                return 2.0;
            case MFS:
                return 5.0;
            case CASH:
                return 8.0;
            default:
                return 0.0;
        }
    }
}

// --- Context / Calculator ---
class DiscountCalculator {
    private final List<DiscountStrategy> strategies;

    public DiscountCalculator(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public double calculateMaxDiscountPercentage(PurchaseRequest request) {
        double maxDiscount = 0.0;
        for (DiscountStrategy strategy : strategies) {
            double discount = strategy.calculateDiscountPercentage(request);
            if (discount > maxDiscount) {
                maxDiscount = discount;
            }
        }
        return maxDiscount;
    }

    public double calculateFinalPayableAmount(PurchaseRequest request) {
        double appliedDiscountPercentage = calculateMaxDiscountPercentage(request);
        return request.getAmount() * (1.0 - (appliedDiscountPercentage / 100.0));
    }
}

// --- Execution / Test Class ---
public class Discount {
    public static void main(String[] args) {
        List<DiscountStrategy> strategies = Arrays.asList(
            new PurchaseAmountDiscountStrategy(),
            new CustomerCategoryDiscountStrategy(),
            new PaymentMethodDiscountStrategy()
        );

        DiscountCalculator calculator = new DiscountCalculator(strategies);

        // Example 1: Amount = 3,500, Customer = PREMIUM, Payment = CASH
        PurchaseRequest purchase1 = new PurchaseRequest(3500, CustomerCategory.PREMIUM, PaymentMethod.CASH);
        processCheckout(calculator, purchase1);

        // Example 2: Amount = 5,500, Customer = PREMIUM, Payment = CASH
        PurchaseRequest purchase2 = new PurchaseRequest(5500, CustomerCategory.PREMIUM, PaymentMethod.CASH);
        processCheckout(calculator, purchase2);
    }

    private static void processCheckout(DiscountCalculator calculator, PurchaseRequest purchase) {
        double maxDiscount = calculator.calculateMaxDiscountPercentage(purchase);
        double finalAmount = calculator.calculateFinalPayableAmount(purchase);

        System.out.println("----------------------------------------");
        System.out.println("Purchase Amount: $" + purchase.getAmount());
        System.out.println("Applied Discount: " + maxDiscount + "%");
        System.out.println("Final Payable Amount: $" + finalAmount);
    }
}