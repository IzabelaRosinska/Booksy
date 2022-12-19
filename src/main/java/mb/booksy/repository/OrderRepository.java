package mb.booksy.repository;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM orders ORDER BY id DESC")
    List<Order> findLastIndex();

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE orders SET payment_id = ?1 WHERE id = ?2")
    void savePayment(Long paymentId, Long orderId);

    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE client_id = ?1 ORDER BY id DESC")
    List<Order> getOrderId(Long id);
}
