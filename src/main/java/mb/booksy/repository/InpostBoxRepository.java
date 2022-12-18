package mb.booksy.repository;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.delivery.InpostBox;
import org.springframework.data.repository.CrudRepository;

public interface InpostBoxRepository extends CrudRepository<InpostBox, Long> {
}
