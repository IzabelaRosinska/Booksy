package mb.booksy.repository;

import mb.booksy.domain.order.cart.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Query(nativeQuery = true, value = "select * from carts where id = ?1")
    Cart findByCartId(Long cartId);

}
