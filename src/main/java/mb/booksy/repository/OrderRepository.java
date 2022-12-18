package mb.booksy.repository;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM orders ORDER BY id DESC")
    List<Order> findLastIndex();
}
