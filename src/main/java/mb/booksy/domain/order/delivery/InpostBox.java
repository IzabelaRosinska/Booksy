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
@Table(name = "inpostBoxes")
public class InpostBox extends BaseEntity {

    @Builder
    public InpostBox(Long id, String boxAddress) {
        super(id);
        this.boxAddress = boxAddress;
    }

    @NotBlank
    @NotEmpty
    @Column(name = "box_address")
    private String boxAddress;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inpostBox")
    private List<InpostDelivery> inpostDeliveries = new ArrayList<>();
}
