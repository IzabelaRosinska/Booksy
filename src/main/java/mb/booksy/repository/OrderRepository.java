package mb.booksy.repository;

import mb.booksy.domain.order.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE orders SET payment_id = ?1 WHERE id = ?2")
    void savePayment(Long paymentId, Long orderId);

    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE client_id = ?1 ORDER BY id DESC")
    List<Order> getOrderId(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE client_id = ?1")
    List<Order> findAllUserOrders(Long userId);

    @Query(nativeQuery = true, value = "SELECT SUM(items_in_carts.number) FROM orders INNER JOIN carts ON orders.cart_id = carts.id INNER JOIN items_in_carts ON carts.id = items_in_carts.cart_id WHERE orders.id = ?1")
    Integer countOrderItems(Long orderId);

    @Query(nativeQuery = true, value = "SELECT SUM(items_in_carts.number * items.price) FROM orders INNER JOIN carts ON orders.cart_id = carts.id INNER JOIN items_in_carts ON carts.id = items_in_carts.cart_id INNER JOIN items ON items.id = items_in_carts.item_id WHERE orders.id = ?1")
    Double countAmount(Long orderId);

    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE id = ?1")
    Order findOrderById(Long valueOf);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE orders SET if_ended = true WHERE id = ?1")
    void setEnded(Long orderId);

    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE cart_id = ?1")
    Order findOrderWithCartId(Long cartId);

    @Query(nativeQuery = true, value = "SELECT cart_id FROM orders WHERE id = ?1")
    Long findCartIdForOrder(Long orderId);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE orders SET delivery_id = ?1 WHERE id = ?2")
    void addDelivery(Long deliveryId, Long orderId);
}
