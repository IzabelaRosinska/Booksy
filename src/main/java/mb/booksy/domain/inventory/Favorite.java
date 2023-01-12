package mb.booksy.domain.inventory;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.cart.ItemInCart;
import mb.booksy.domain.user.Client;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "favorite")
public class Favorite extends BaseEntity {

    @Builder
    public Favorite(Long id, Client clientId, Item itemId) {
        super(id);
        this.clientId = clientId;
        this.itemId = itemId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item itemId;
}
