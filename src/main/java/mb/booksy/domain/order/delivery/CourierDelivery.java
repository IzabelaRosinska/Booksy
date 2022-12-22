package mb.booksy.domain.order.delivery;

import lombok.*;
import mb.booksy.domain.BaseEntity;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "courierDeliveries")
public class CourierDelivery extends BaseEntity {

    @Builder
    public CourierDelivery(Long id, String deliveryAddress) {
        super(id);
        this.deliveryAddress = deliveryAddress;
    }

    @Column(name = "delivery_address")
    String deliveryAddress;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courierDelivery")
    private Set<Delivery> deliveries = new HashSet<>();
}
