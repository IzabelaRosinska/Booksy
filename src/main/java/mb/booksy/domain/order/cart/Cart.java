package mb.booksy.domain.order.cart;

import lombok.*;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.Order;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    @Builder
    public Cart(Long id) {
        super(id);
        this.initDate = LocalDate.now();
        this.itemNumber = 0;
    }

    @Column(name = "init_date")
    private LocalDate initDate;

    @Column(name = "item_number")
    private Integer itemNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private Set<ItemInCart> itemsCart = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private Set<Order> orders = new HashSet<>();
}
