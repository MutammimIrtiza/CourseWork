// State Interface / Base Abstract Class
abstract class ReturnState {
    protected ReturnRequest request;

    public ReturnState(ReturnRequest request) {
        this.request = request;
    }

    public void updateReason(String reason) {
        System.out.println("Invalid operation: Cannot update reason in state [" + getStateName() + "].");
    }

    public void approve() {
        System.out.println("Invalid operation: Cannot approve request in state [" + getStateName() + "].");
    }

    public void reject() {
        System.out.println("Invalid operation: Cannot reject request in state [" + getStateName() + "].");
    }

    public void cancel() {
        System.out.println("Invalid operation: Cannot cancel request in state [" + getStateName() + "].");
    }

    public void itemDelivered() {
        System.out.println("Invalid operation: Cannot mark item delivered in state [" + getStateName() + "].");
    }

    public void inspect(boolean eligible) {
        System.out.println("Invalid operation: Cannot inspect item in state [" + getStateName() + "].");
    }

    public void refundSuccessful() {
        System.out.println("Invalid operation: Cannot execute refund successful in state [" + getStateName() + "].");
    }

    public void refundFailed() {
        System.out.println("Invalid operation: Cannot execute refund failed in state [" + getStateName() + "].");
    }

    public abstract String getStateName();
}

// Concrete State: Requested
class RequestedState extends ReturnState {
    public RequestedState(ReturnRequest request) {
        super(request);
    }

    @Override
    public void updateReason(String reason) {
        request.setReason(reason);
        System.out.println("Return reason updated to: \"" + reason + "\"");
    }

    @Override
    public void approve() {
        request.setState(new ApprovedState(request));
        System.out.println("Request approved.");
    }

    @Override
    public void reject() {
        request.setState(new RejectedState(request));
        System.out.println("Request rejected.");
    }

    @Override
    public void cancel() {
        request.setState(new CancelledState(request));
        System.out.println("Request cancelled.");
    }

    @Override
    public String getStateName() {
        return "Requested";
    }
}

// Concrete State: Approved
class ApprovedState extends ReturnState {
    public ApprovedState(ReturnRequest request) {
        super(request);
    }

    @Override
    public void cancel() {
        request.setState(new CancelledState(request));
        System.out.println("Request cancelled.");
    }

    @Override
    public void itemDelivered() {
        request.setState(new DeliveredState(request));
        System.out.println("Returned item marked as delivered.");
    }

    @Override
    public String getStateName() {
        return "Approved";
    }
}

// Concrete State: Delivered
class DeliveredState extends ReturnState {
    public DeliveredState(ReturnRequest request) {
        super(request);
    }

    @Override
    public void inspect(boolean eligible) {
        if (eligible) {
            request.setState(new ProcessingRefundState(request));
            System.out.println("Item inspected and eligible. Transitioned to Processing Refund.");
        } else {
            request.setState(new RejectedState(request));
            System.out.println("Item inspected and ineligible under return policy. Request rejected.");
        }
    }

    @Override
    public String getStateName() {
        return "Delivered";
    }
}

// Concrete State: Processing Refund
class ProcessingRefundState extends ReturnState {
    public ProcessingRefundState(ReturnRequest request) {
        super(request);
    }

    @Override
    public void refundSuccessful() {
        request.setState(new RefundedState(request));
        System.out.println("Refund processed successfully.");
    }

    @Override
    public void refundFailed() {
        System.out.println("Refund attempt failed. Retrying can be attempted later.");
    }

    @Override
    public String getStateName() {
        return "Processing Refund";
    }
}

// Base class for Final States (Refunded, Rejected, Cancelled)
abstract class FinalState extends ReturnState {
    public FinalState(ReturnRequest request) {
        super(request);
    }
}

class RefundedState extends FinalState {
    public RefundedState(ReturnRequest request) { super(request); }
    @Override public String getStateName() { return "Refunded"; }
}

class RejectedState extends FinalState {
    public RejectedState(ReturnRequest request) { super(request); }
    @Override public String getStateName() { return "Rejected"; }
}

class CancelledState extends FinalState {
    public CancelledState(ReturnRequest request) { super(request); }
    @Override public String getStateName() { return "Cancelled"; }
}

// Context Class
class ReturnRequest {
    private final String requestId;
    private String reason;
    private ReturnState currentState;

    public ReturnRequest(String requestId, String reason) {
        this.requestId = requestId;
        this.reason = reason;
        this.currentState = new RequestedState(this);
    }

    public void setState(ReturnState state) {
        this.currentState = state;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCurrentStateName() {
        return currentState.getStateName();
    }

    // Context delegates operations to current state
    public void updateReason(String reason) { currentState.updateReason(reason); }
    public void approve() { currentState.approve(); }
    public void reject() { currentState.reject(); }
    public void cancel() { currentState.cancel(); }
    public void itemDelivered() { currentState.itemDelivered(); }
    public void inspect(boolean eligible) { currentState.inspect(eligible); }
    public void refundSuccessful() { currentState.refundSuccessful(); }
    public void refundFailed() { currentState.refundFailed(); }
}

// Main Execution / Demonstration
public class Ecommerce {
    public static void main(String[] args) {
        ReturnRequest request = new ReturnRequest("REQ-1001", "Defective screen");

        System.out.println("--- Scenario 1: Successful Return & Refund ---");
        request.updateReason("Defective screen and flickering light"); // Allowed in Requested state[cite: 1]
        request.approve();                                            // Transitions to Approved[cite: 1]
        request.updateReason("Changed mind");                          // Invalid in Approved state[cite: 1]
        request.itemDelivered();                                       // Transitions to Delivered[cite: 1]
        request.cancel();                                             // Invalid in Delivered state[cite: 1]
        request.inspect(true);                                        // Transitions to Processing Refund[cite: 1]
        request.refundFailed();                                       // Remains in Processing Refund[cite: 1]
        request.refundSuccessful();                                   // Transitions to Refunded[cite: 1]
        request.cancel();                                             // Invalid in Final State[cite: 1]

        System.out.println("\n--- Scenario 2: Failed Inspection ---");
        ReturnRequest failedInspectionReq = new ReturnRequest("REQ-1002", "Wrong size");
        failedInspectionReq.approve();
        failedInspectionReq.itemDelivered();
        failedInspectionReq.inspect(false);                          // Transitions to Rejected[cite: 1]
        failedInspectionReq.approve();                                // Invalid in Final State[cite: 1]
    }
}