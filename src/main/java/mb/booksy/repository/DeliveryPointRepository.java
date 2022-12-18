package mb.booksy.repository;

import mb.booksy.domain.order.delivery.DeliveryPoint;
import mb.booksy.domain.order.delivery.InpostBox;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryPointRepository extends CrudRepository<DeliveryPoint, Long> {
}
