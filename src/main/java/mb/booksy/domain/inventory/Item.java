package mb.booksy.domain.inventory;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.cart.ItemInCart;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item extends BaseEntity {

    @Builder
    public Item(Long id, String itemName, String producerName, Float price, Integer availability) {
        super(id);
        this.itemName = itemName;
        this.producerName = producerName;
        this.price = price;
        this.availability = availability;
    }

    @NotBlank
    @NotEmpty
    @Column(name = "item_name")
    private String itemName;

    @NotBlank
    @NotEmpty
    @Column(name = "producer_name")
    private String producerName;

    @NotEmpty
    @Column(name = "price")
    private Float price;

    @NotEmpty
    @Column(name = "availability")
    private Integer availability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private List<ItemInCart> cartItems = new ArrayList<>();
}
