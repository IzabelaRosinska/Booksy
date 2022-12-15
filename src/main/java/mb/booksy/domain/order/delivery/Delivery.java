package mb.booksy.domain.order.delivery;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.inventory.Offer;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

    @Builder
    public Delivery(Long id) {
        super(id);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_company_id")
    private DeliveryCompany deliveryCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inpost_delivery_id")
    private InpostDelivery inpostDelivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_delivery_id")
    private CourierDelivery courierDelivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_delivery_id")
    private PointDelivery pointDelivery;
}
