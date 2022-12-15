package mb.booksy.domain.order;

public enum OrderStatus {

    DELIVERED("dostarczone"),
    PACKED("przygotowane do wysłania"),
    IN_PROGRESS("w trakcie przygotowania"),
    ORDERED("złożone");

    String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
