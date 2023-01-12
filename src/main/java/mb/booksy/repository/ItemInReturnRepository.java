package mb.booksy.repository;
import mb.booksy.domain.order.ItemInReturn;
import mb.booksy.domain.order.OrderReturn;
import mb.booksy.domain.order.cart.ItemInCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemInReturnRepository extends CrudRepository<ItemInReturn, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM items_in_returns INNER JOIN items ON items_in_returns.item_id = items.id INNER JOIN order_returns ON items_in_returns.order_return_id = order_returns.id WHERE (items_in_returns.item_id = ?1 AND items_in_returns.order_return_id = ?2)")
    List<ItemInReturn> findItemInOrderReturn(Long itemId, Long orderReturnId);

    @Query(nativeQuery = true, value = "SELECT * FROM items_in_returns WHERE items_in_returns.order_return_id = ?1")
    List<ItemInReturn> findItemsInOrderReturn(Long orderReturnId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM items_in_returns WHERE (order_return_id = ?1 AND item_id = ?2)")
    void deleteItemInOrderReturn(Long orderReturnId, Long itemId);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE items_in_returns SET items_in_returns.number = ?1 WHERE (items_in_returns.item_id = ?3 AND items_in_returns.order_return_id = ?2)")
    void updateItemNumber(Integer new_number, Long orderReturnId, Long itemId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM items_in_returns WHERE items_in_returns.order_return_id = ?1")
    Integer countItemsInOrderReturn(Long orderReturnId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM items_in_returns WHERE order_return_id = ?1")
    void deleteOrderReturnItems(Long orderReturnId);
}
