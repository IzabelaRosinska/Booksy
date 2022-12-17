package mb.booksy.domain.order.delivery;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "deliveryPoints")
public class DeliveryPoint extends BaseEntity {

    @Builder
    public DeliveryPoint(Long id, String point_name, String addressName, String contact) {
        super(id);
        this.pointName = pointName;
        this.addressName = addressName;
        this.contact = contact;
    }

    @NotBlank
    @NotEmpty
    @Column(name = "point_name")
    String pointName;

    @NotBlank
    @NotEmpty
    @Column(name = "address_name")
    String addressName;

    @NotBlank
    @NotEmpty
    @Column(name = "contact")
    String contact;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deliveryPoint")
    private Set<PointDelivery> pointDeliveries = new HashSet<>();
}
