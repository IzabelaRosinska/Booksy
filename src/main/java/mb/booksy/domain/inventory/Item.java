package mb.booksy.domain.inventory;

import lombok.*;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.order.cart.ItemInCart;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item extends BaseEntity {

    @Builder
    public Item(Long id, String itemName, String producerName, Double price, Integer availability, byte[] itemImage) {
        super(id);
        this.itemName = itemName;
        this.producerName = producerName;
        this.price = price;
        this.availability = availability;
        this.itemImage = itemImage;
    }

    @NotBlank
    @NotEmpty
    @Column(name = "item_name")
    private String itemName;

    @NotBlank
    @NotEmpty
    @Column(name = "producer_name")
    private String producerName;


    @Column(name = "price")
    private Double price;

    @Column(name = "availability")
    private Integer availability;

    @Lob
    @Column(name = "image")
    private byte[] itemImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private Set<ItemInCart> cartItems = new HashSet<>();

    public byte[] getItemImage() {
        return itemImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemName.equals(item.itemName) && producerName.equals(item.producerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, producerName);
    }
}
