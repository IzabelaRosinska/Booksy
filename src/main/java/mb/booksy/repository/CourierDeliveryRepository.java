package mb.booksy.repository;

import mb.booksy.domain.order.delivery.CourierDelivery;
import mb.booksy.domain.order.delivery.InpostDelivery;
import org.springframework.data.repository.CrudRepository;

public interface CourierDeliveryRepository extends CrudRepository<CourierDelivery, Long> {
}
