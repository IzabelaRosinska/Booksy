package mb.booksy.repository;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.cart.ItemInCart;
import org.springframework.data.repository.CrudRepository;

public interface ItemInCartRepository  extends CrudRepository<ItemInCart, Long> {
}
