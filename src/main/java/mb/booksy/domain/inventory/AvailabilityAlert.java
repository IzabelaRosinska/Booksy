package mb.booksy.domain.inventory;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mb.booksy.domain.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "availability_alert")

public class AvailabilityAlert extends BaseEntity {

    @Builder
    public AvailabilityAlert(Long id, String mail, Item item) {
        super(id);
        this.mail = mail;
        this.item = item;
    }

    @Column(name = "mail")
    private String mail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
