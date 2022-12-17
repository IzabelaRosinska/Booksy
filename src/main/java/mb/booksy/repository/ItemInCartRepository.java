package mb.booksy.repository;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.cart.ItemInCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemInCartRepository  extends CrudRepository<ItemInCart, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM items_in_carts INNER JOIN items ON items_in_carts.item_id = items.id INNER JOIN carts ON items_in_carts.cart_id = carts.id WHERE (items_in_carts.item_id = ?1 AND items_in_carts.cart_id = ?2)")
    List<ItemInCart> findItemInCart(Long itemId, Long cartId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM items_in_carts WHERE (cart_id = ?1 AND item_id = ?2)")
    void deleteItemInCart(Long cartId, Long itemId);
}
