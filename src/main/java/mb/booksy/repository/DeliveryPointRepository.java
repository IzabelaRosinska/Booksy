package mb.booksy.repository;

import mb.booksy.domain.order.delivery.DeliveryPoint;
import mb.booksy.domain.order.delivery.InpostBox;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveryPointRepository extends CrudRepository<DeliveryPoint, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM delivery_points")
    List<DeliveryPoint> findAll();

    @Query(nativeQuery = true, value = "SELECT * FROM delivery_points WHERE id = ?1")
    DeliveryPoint findByPointId(Long pointId);
}
