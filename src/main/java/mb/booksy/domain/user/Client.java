package mb.booksy.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mb.booksy.domain.order.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends Person {

    @Builder
    public Client(Long id, String login, String name, String surname, String telephone, String email, String account, String password, String address, SimpleGrantedAuthority userRole) {
        super(id, login, name, surname, password, userRole);
        this.telephone = telephone;
        this.email = email;
        this.address = address;
        this.account = account;
        this.loyaltyPoints = 0;
    }

    @NotEmpty
    @Column(name = "telephone")
    private String telephone;

    @NotEmpty
    @Column(name = "email")
    private String email;

    @NotEmpty
    @Column(name = "account")
    private String account;

    @Column(name = "address")
    private String address;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private List<Order> orders = new ArrayList<>();
}
