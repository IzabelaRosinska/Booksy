package mb.booksy.domain.order;

import lombok.*;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.order.delivery.Delivery;
import mb.booksy.domain.order.payment.Payment;
import mb.booksy.domain.user.Client;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Builder
    public Order(Long id, LocalDate orderDate, Client client, Cart cart, Boolean ifEnded) {
        super(id);
        this.orderDate = orderDate;
        this.client = client;
        this.cart = cart;
        this.ifEnded = ifEnded;
    }

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "if_ended")
    private Boolean ifEnded;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_surname")
    private String receiverSurname;

    @Column(name = "receiver_mail")
    private String receiverMail;

    @Column(name = "receiver_phone")
    private String receiverPhone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<Complaint> complaints = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderReturn> orderReturns = new HashSet<>();

    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
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