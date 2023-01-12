package mb.booksy.domain.order;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.cart.ItemInCart;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "order_returns")
public class OrderReturn extends BaseEntity {

    @Builder
    public OrderReturn(Long id, Boolean acceptance, LocalDate initDate, LocalDate processDate) {
        super(id);
        this.acceptance = acceptance;
        this.initDate = initDate;
        this.processDate = processDate;
    }

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "acceptance")
    private Boolean acceptance;

    @Column(name = "init_date")
    private LocalDate initDate;

    @Column(name = "process_date")
    private LocalDate processDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderReturn")
    private Set<ItemInReturn> itemsReturn = new HashSet<>();
}
