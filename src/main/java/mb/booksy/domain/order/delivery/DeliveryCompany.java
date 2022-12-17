package mb.booksy.domain.order.delivery;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import mb.booksy.domain.inventory.Offer;
import mb.booksy.domain.order.cart.ItemInCart;

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
@Table(name = "deliveryCompanies")
public class DeliveryCompany extends BaseEntity {

    @Builder
    public DeliveryCompany(Long id, String companyName) {
        super(id);
        this.companyName = companyName;
    }

    @NotBlank
    @NotEmpty
    @Column(name = "company_name")
    private String companyName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deliveryCompany")
    private Set<Delivery> deliveries = new HashSet<>();
}
