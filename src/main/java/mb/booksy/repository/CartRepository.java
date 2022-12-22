package mb.booksy.repository;

import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.user.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM carts WHERE client_id = ?1")
    List<Cart> findClCarts(Long clientId);

    @Query(nativeQuery = true, value = "select * from carts where id = ?1")
    Cart findByCartId(Long cartId);

    @Query(nativeQuery = true, value = "select item_number from carts where id = ?1")
    Integer findCartSize(Long cartId);
}
