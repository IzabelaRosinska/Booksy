package mb.booksy.repository;

import mb.booksy.domain.order.OrderReturn;
import mb.booksy.domain.order.cart.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderReturnRepository extends CrudRepository<OrderReturn, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM order_returns INNER JOIN orders ON order_returns.order_id = orders.id INNER JOIN clients ON orders.client_id = clients.id WHERE clients.id = ?1")
    List<OrderReturn> findAllByUserId(Long clientId);

    @Query(nativeQuery = true, value = "select * from order_returns where id = ?1")
    OrderReturn findByOrderReturnId(Long orderReturnId);

    @Query(nativeQuery = true, value = "select * from order_returns where order_id = ?1")
    List<OrderReturn> findByOrderId(Long orderId);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE order_returns SET order_returns.is_valid = true WHERE id = ?1")
    void validateOrderReturn(Long orderReturnId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM order_returns WHERE id = ?1")
    void deleteOrderReturn(Long orderReturnId);
}
