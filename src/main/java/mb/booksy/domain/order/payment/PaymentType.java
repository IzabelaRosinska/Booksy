package mb.booksy.domain.order.payment;

public enum PaymentType {

    PAYU("PayU"),
    BLIK("Blik");

    String paymentType;

    PaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
