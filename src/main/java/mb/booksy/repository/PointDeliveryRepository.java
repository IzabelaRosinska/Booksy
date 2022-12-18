package mb.booksy.repository;

import mb.booksy.domain.order.delivery.CourierDelivery;
import mb.booksy.domain.order.delivery.PointDelivery;
import org.springframework.data.repository.CrudRepository;

public interface PointDeliveryRepository extends CrudRepository<PointDelivery, Long> {
}
