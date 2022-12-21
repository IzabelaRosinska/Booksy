package mb.booksy.repository;

import mb.booksy.domain.order.delivery.Delivery;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryRepository extends CrudRepository<Delivery, Long> {

}
