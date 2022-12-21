package mb.booksy.domain.order.delivery;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pointDeliveries")
public class PointDelivery extends BaseEntity {

    @Builder
    public PointDelivery(Long id, DeliveryPoint deliveryPoint) {
        super(id);
        this.deliveryPoint = deliveryPoint;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private DeliveryPoint deliveryPoint;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pointDelivery")
    private Set<Delivery> deliveries = new HashSet<>();
}
