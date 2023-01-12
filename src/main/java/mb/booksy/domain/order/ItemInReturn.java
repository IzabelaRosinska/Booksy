package mb.booksy.domain.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.cart.Cart;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items_in_returns")
public class ItemInReturn extends BaseEntity {

    @Builder
    public ItemInReturn(Long id, Integer number, Item item, OrderReturn orderReturn) {
        super(id);
        this.number = number;
        this.item = item;
        this.orderReturn = orderReturn;
    }

    @Column(name = "number")
    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_return_id")
    private OrderReturn orderReturn;
}
