package mb.booksy.domain.order;

import lombok.*;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.order.delivery.Delivery;
import mb.booksy.domain.order.payment.Payment;
import mb.booksy.domain.user.Client;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Builder
    public Order(Long id, Boolean czyPrawidlowe, Boolean czyZakonczone) {
        super(id);
        this.czyPrawidlowe = czyPrawidlowe;
        this.czyZakonczone = czyZakonczone;
    }

    @Column(name = "czy_prawidlowe")
    private Boolean czyPrawidlowe;

    @Column(name = "czy_zakonczone")
    private Boolean czyZakonczone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<Complaint> complaints = new ArrayList<>();

    @NotNull
    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
}