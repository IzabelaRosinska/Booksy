package mb.booksy.domain.order.delivery;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
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
    private List<Delivery> deliveries = new ArrayList<>();
}
