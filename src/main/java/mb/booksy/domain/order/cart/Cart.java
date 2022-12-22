package mb.booksy.domain.order.cart;

import lombok.*;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.delivery.DeliveryCompany;
import mb.booksy.domain.user.Client;

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
    public Cart(Long id, Client client, Integer itemNumber, LocalDate initDate) {
        super(id);
        this.initDate = initDate;
        this.itemNumber = itemNumber;
        this.client = client;
        this.points = 0;
    }

    @Column(name = "init_date")
    private LocalDate initDate;

    @Column(name = "item_number")
    private Integer itemNumber;

    @Column(name = "loyalty_points")
    private Integer points;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private Set<ItemInCart> itemsCart = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cart")
    private Order order;
}
