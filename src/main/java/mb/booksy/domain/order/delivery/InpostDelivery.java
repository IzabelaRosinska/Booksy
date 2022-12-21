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
@Table(name = "inpostDeliveries")
public class InpostDelivery extends BaseEntity {

    @Builder
    public InpostDelivery(Long id, InpostBox inpostBox) {
        super(id);
        this.inpostBox = inpostBox;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id")
    private InpostBox inpostBox;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inpostDelivery")
    private Set<Delivery> deliveries = new HashSet<>();
}
