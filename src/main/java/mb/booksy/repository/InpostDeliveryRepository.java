package mb.booksy.repository;

import mb.booksy.domain.order.delivery.InpostBox;
import mb.booksy.domain.order.delivery.InpostDelivery;
import org.springframework.data.repository.CrudRepository;

public interface InpostDeliveryRepository extends CrudRepository<InpostDelivery, Long> {
}
