package mb.booksy.domain.inventory;

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
@Table(name = "offers")
public class Offer extends BaseEntity {

    @Builder
    public Offer(Long id, String offerName) {
        super(id);
        this.offerName = offerName;
    }

    @NotBlank
    @NotEmpty
    @Column(name = "offer_name")
    private String offerName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offer")
    private List<Item> items = new ArrayList<>();
}
