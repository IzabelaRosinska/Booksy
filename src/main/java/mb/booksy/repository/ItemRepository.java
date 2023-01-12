package mb.booksy.repository;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.delivery.DeliveryPoint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.Collection;
import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM items INNER JOIN items_in_carts ON items_in_carts.item_id = items.id WHERE items_in_carts.cart_id = ?1")
    List<Item> findAllInCart(Long cartId);

    @Query(nativeQuery = true, value = "SELECT * FROM items INNER JOIN items_in_carts ON items.id = items_in_carts.item_id INNER JOIN carts ON items_in_carts.cart_id = carts.id INNER JOIN orders ON carts.id = orders.cart_id  WHERE orders.id = ?1")
    Collection<Item> findAllInOrder(String orderId);

    @Query(nativeQuery = true, value = "SELECT * FROM items WHERE id = ?1")
    Item findByItemId(Long itemId);

    @Query(nativeQuery = true, value = "SELECT * FROM items")
    List<Item> findAll();
}
