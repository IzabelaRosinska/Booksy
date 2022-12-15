package mb.booksy.domain.order.cart;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.Order;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
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

    @NotEmpty
    @Column(name = "init_date")
    private LocalDate initDate;

    @NotEmpty
    @Column(name = "item_number")
    private Integer itemNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private List<ItemInCart> itemsCart = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private List<Order> orders = new ArrayList<>();
}
