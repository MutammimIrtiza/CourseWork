package service;

import model.DeliveryType;
import model.MenuItem;
import model.Order;
import model.OrderBuilder;
import model.OrderItem;
import model.PaymentMethod;
import model.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates order creation.
 *
 * Several methods below repeat long Order constructor calls with many optional
 * parameters. That is intentional assignment material for refactoring.
 */
public class OrderService {
    private int nextNumber = 1001;

    public OrderItem createOrderItem(MenuItem item, int quantity, Size size, boolean extraCheese, boolean spicy, String note) {
        return new OrderItem(item, quantity, size, extraCheese, spicy, note);
    }

    public Order createDeliveryOrder(String customerName,
                                     String phone,
                                     String address,
                                     List<OrderItem> items,
                                     String couponCode,
                                     boolean rushOrder,
                                     String specialInstructions) {
        // return new Order(nextOrderId(), customerName, phone,
        //         DeliveryType.DELIVERY,
        //         address,
        //         PaymentMethod.CASH,
        //         null,
        //         couponCode,
        //         false,
        //         true,
        //         0,
        //         rushOrder,
        //         items,
        //         specialInstructions);

        return new OrderBuilder().setOrderId(nextOrderId())
                                 .setCustomerName(customerName)
                                 .setPhone(phone)
                                 .setDeliveryType(DeliveryType.DELIVERY)
                                 .setDeliveryAddress(address)
                                 .setItems(items)
                                 .setCouponCode(couponCode)
                                 .setLoyaltyPointsToRedeem(0)
                                 .setRushOrder(rushOrder)
                                 .setSpecialInstructions(specialInstructions)
                                 .build();


    }

    public Order createPickupOrder(String customerName, String phone, List<OrderItem> items) {
        // return new Order(nextOrderId(), customerName, phone,
        //         DeliveryType.PICKUP,
        //         "",
        //         PaymentMethod.CASH,
        //         null,
        //         "",
        //         false,
        //         true,
        //         0,
        //         false,
        //         items,
        //         "");

        return new OrderBuilder().setOrderId(nextOrderId())
                                 .setCustomerName(customerName)
                                 .setPhone(phone)
                                 .setDeliveryType(DeliveryType.PICKUP)
                                 .setItems(items)
                                 .setLoyaltyPointsToRedeem(0)
                                 .setRushOrder(false)
                                 .build();

    }

    public Order createScheduledGiftOrder(String customerName,
                                          String phone,
                                          String address,
                                          List<OrderItem> items,
                                          LocalDateTime scheduledTime) {
        // return new Order(nextOrderId(), customerName, phone,
        //         DeliveryType.DELIVERY,
        //         address,
        //         PaymentMethod.CARD,
        //         scheduledTime,
        //         "WELCOME10",
        //         true,
        //         false,
        //         25,
        //         false,
        //         items,
        //         "Please call before delivery");

        return new OrderBuilder().setOrderId(nextOrderId())
                                 .setCustomerName(customerName)
                                 .setPhone(phone)
                                 .setDeliveryType(DeliveryType.DELIVERY)
                                 .setDeliveryAddress(address)
                                 .setPaymentMethod(PaymentMethod.CARD)
                                 .setScheduledTime(scheduledTime)
                                 .setCouponCode("WELCOME10")
                                 .setGiftWrap(true)
                                 .setCutleryRequired(false)
                                 .setLoyaltyPointsToRedeem(25)
                                 .setRushOrder(false)
                                 .setItems(items)
                                 .setSpecialInstructions("Please call before delivery")
                                 .build();


    }

    public Order createSampleFamilyOrder(MenuCatalog catalog) {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(catalog.findByCode("P01"), 2, Size.LARGE, true, false, "half spicy"));
        items.add(new OrderItem(catalog.findByCode("B02"), 3, Size.MEDIUM, true, true, ""));
        items.add(new OrderItem(catalog.findByCode("D02"), 4, Size.MEDIUM, false, false, "less sugar"));
        items.add(new OrderItem(catalog.findByCode("S02"), 2, Size.LARGE, false, true, ""));

        // return new Order(nextOrderId(),
        //         "Sample Family",
        //         "01711111111",
        //         DeliveryType.DELIVERY,
        //         "House 25, Road 4, Dhanmondi",
        //         PaymentMethod.MOBILE_BANKING,
        //         null,
        //         "FAMILY15",
        //         false,
        //         true,
        //         50,
        //         true,
        //         items,
        //         "Deliver together");

            
        return new OrderBuilder().setOrderId(nextOrderId())
                                 .setCustomerName("Sample Family")
                                 .setPhone("01711111111")
                                 .setDeliveryType(DeliveryType.DELIVERY)
                                 .setDeliveryAddress("House 25, Road 4, Dhanmondi")
                                 .setPaymentMethod(PaymentMethod.MOBILE_BANKING)
                                 .setCouponCode("FAMILY15")
                                 .setLoyaltyPointsToRedeem(50)
                                 .setRushOrder(true)
                                 .setItems(items)
                                 .setSpecialInstructions("Deliver together")
                                 .build();

    }

    private String nextOrderId() {
        return "FF-" + nextNumber++;
    }
}

