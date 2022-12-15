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
@Table(name = "inpostDeliveries")
public class InpostDelivery extends BaseEntity {

    @Builder
    public InpostDelivery(Long id) {
        super(id);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id")
    private InpostBox inpostBox;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inpostDelivery")
    private List<Delivery> deliveries = new ArrayList<>();
}
