package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OrderBuilder {
    
    private String orderId;
    private String customerName;
    private String phone;
    private DeliveryType deliveryType = DeliveryType.PICKUP;
    private String deliveryAddress = "";
    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    private LocalDateTime scheduledTime = null;
    private String couponCode = "";
    private boolean giftWrap = false;
    private boolean cutleryRequired = true;
    private int loyaltyPointsToRedeem;
    private boolean rushOrder;
    private List<OrderItem> items;
    private String specialInstructions = "";

    public OrderBuilder setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }


    public OrderBuilder setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }


    public OrderBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }


    public OrderBuilder setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
        return this;
    }


    public OrderBuilder setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }


    public OrderBuilder setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }


    public OrderBuilder setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }


    public OrderBuilder setCouponCode(String couponCode) {
        this.couponCode = couponCode != null ? couponCode.trim().toUpperCase() : "";
        return this;
    }


    public OrderBuilder setGiftWrap(boolean giftWrap) {
        this.giftWrap = giftWrap;
        return this;
    }


    public OrderBuilder setCutleryRequired(boolean cutleryRequired) {
        this.cutleryRequired = cutleryRequired;
        return this;
    }


    public OrderBuilder setLoyaltyPointsToRedeem(int loyaltyPointsToRedeem) {
        this.loyaltyPointsToRedeem = Math.max(0, loyaltyPointsToRedeem);
        return this;
    }


    public OrderBuilder setRushOrder(boolean rushOrder) {
        this.rushOrder = rushOrder;
        return this;
    }


    public OrderBuilder setItems(List<OrderItem> items) {
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
        return this;
    }


    public OrderBuilder setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions != null ? specialInstructions.trim() : "";
        return this;
    }


    private static String requireNonBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return trimmed;
    }


    private void validate() {
        orderId = requireNonBlank(orderId, "Order id");

        customerName = requireNonBlank(customerName, "Customer name");

        phone = requireNonBlank(phone, "Phone");

        if (this.deliveryType == DeliveryType.DELIVERY) {
            this.deliveryAddress = requireNonBlank(deliveryAddress, "Delivery address");
        } else {
            this.deliveryAddress = deliveryAddress != null ? deliveryAddress.trim() : "";
        }

        Objects.requireNonNull(items, "Items cannot be null");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

    }


    public Order build() {
        validate();
        return new Order(orderId, 
                     customerName,
                     phone, 
                     deliveryType,
                     deliveryAddress,
                     paymentMethod, 
                     scheduledTime,
                     couponCode,
                     giftWrap,
                     cutleryRequired,
                     loyaltyPointsToRedeem,
                     rushOrder,
                     items,
                     specialInstructions
        );
    }

}
