package mb.booksy.domain.order.payment;

public enum Currency {

    PLN("PLN"),
    EUR("EUR"),
    GPB("GPB");

    String currency_type;

    Currency(String currency_type) {
        this.currency_type = currency_type;
    }
}
