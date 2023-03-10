package mb.booksy.domain.order.delivery;

import lombok.*;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.inventory.Offer;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

    @Builder
    public Delivery(Long id, DeliveryCompany deliveryCompany, InpostDelivery inpostDelivery, CourierDelivery courierDelivery, PointDelivery pointDelivery) {
        super(id);
        this.deliveryCompany = deliveryCompany;
        this.inpostDelivery = inpostDelivery;
        this.courierDelivery = courierDelivery;
        this.pointDelivery = pointDelivery;
    }

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
