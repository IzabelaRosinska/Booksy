package mb.booksy.repository;


import mb.booksy.domain.order.delivery.CourierDelivery;
import mb.booksy.domain.order.delivery.Delivery;
import mb.booksy.domain.order.delivery.InpostDelivery;
import mb.booksy.domain.order.delivery.PointDelivery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DeliveryRepository extends CrudRepository<Delivery, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM deliveries ORDER BY id DESC")
    List<Delivery> findLastIndex();

    @Query(nativeQuery = true, value = "SELECT * FROM inpost_deliveries ORDER BY id DESC")
    List<InpostDelivery> findLastInpostDeliveryIndex();

    @Query(nativeQuery = true, value = "SELECT * FROM courier_deliveries ORDER BY id DESC")
    List<CourierDelivery> findLastCourierDeliveryIndex();

    @Query(nativeQuery = true, value = "SELECT * FROM point_deliveries ORDER BY id DESC")
    List<PointDelivery> findLastPointDeliveryIndex();
}
