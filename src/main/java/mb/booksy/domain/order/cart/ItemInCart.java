package mb.booksy.domain.order.cart;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.inventory.Item;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@Entity
@Table(name = "itemsInCarts")
public class ItemInCart extends BaseEntity {

    @Builder
    public ItemInCart(Long id, Integer number, Item item, Cart cart) {
        super(id);
        this.number = number;
        this.cart = cart;
        this.item = item;
    }

    @NotEmpty
    @Column(name = "number")
    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
