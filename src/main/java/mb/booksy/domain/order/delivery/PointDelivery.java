package mb.booksy.domain.order.delivery;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pointDeliveries")
public class PointDelivery extends BaseEntity {

    @Builder
    public PointDelivery(Long id) {
        super(id);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private DeliveryPoint deliveryPoint;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pointDelivery")
    private List<Delivery> deliveries = new ArrayList<>();
}
