package mb.booksy.repository;

import mb.booksy.domain.inventory.AvailabilityAlert;
import org.springframework.data.repository.CrudRepository;

public interface AvailabilityAlertRepository extends CrudRepository<AvailabilityAlert, Long> {
}
