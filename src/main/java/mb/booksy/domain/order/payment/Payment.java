package mb.booksy.domain.order.payment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.Order;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Builder
    public Payment(Long id, Double amount, LocalDate paymentDate, PaymentType paymentType, Currency currency) {
        super(id);
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.currency = currency;
    }

    @Column(name = "amount")
    private Double amount;


    @Column(name = "payment_date")
    private LocalDate paymentDate;


    @Column(name = "currency")
    @Enumerated(value = EnumType.STRING)
    private Currency currency;


    @Column(name = "payment_type")
    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "payment")
    private Set<Order> orders = new HashSet<>();
}
