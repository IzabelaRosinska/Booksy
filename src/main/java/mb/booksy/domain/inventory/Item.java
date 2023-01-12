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
    public Item(Long id, String itemName, String producerName, String genre, String bookType, Double price, Integer availability, byte[] itemImage, String itemDescription) {
        super(id);
        this.itemName = itemName;
        this.producerName = producerName;
        this.genre = genre;
        this.bookType = bookType;
        this.price = price;
        this.availability = availability;
        this.itemImage = itemImage;
        this.itemDescription = itemDescription;
    }

    @NotBlank
    @NotEmpty
    @Column(name = "item_name")
    private String itemName;

    @NotBlank
    @NotEmpty
    @Column(name = "producer_name")
    private String producerName;

    @Column(name = "genre")
    private String genre;

    @Column(name = "book_type")
    private String bookType;

    @Column(name = "price")
    private Double price;

    @Column(name = "availability")
    private Integer availability;

    @Lob
    @Column(name = "image")
    private byte[] itemImage;

    @NotBlank
    @NotEmpty
    @Column(name = "item_description")
    private String itemDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private Set<ItemInCart> cartItems = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
    private Set<Favorite> favorites = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private Set<AvailabilityAlert> alerts = new HashSet<>();

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
