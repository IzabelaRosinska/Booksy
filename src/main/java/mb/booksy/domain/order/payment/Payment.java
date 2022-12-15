package mb.booksy.domain.order.payment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.Order;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Builder
    public Payment(Long id, Float amount) {
        super(id);
        this.amount = amount;
        this.paymentDate = LocalDate.now();
    }

    @NotEmpty
    @Column(name = "amount")
    private Float amount;

    @NotEmpty
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @NotNull
    @Column(name = "currency")
    @Enumerated(value = EnumType.STRING)
    private Currency currency;

    @NotNull
    @Column(name = "payment_type")
    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "payment")
    private List<Order> orders = new ArrayList<>();
}
