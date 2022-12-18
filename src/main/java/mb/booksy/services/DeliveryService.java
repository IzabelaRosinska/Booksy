package mb.booksy.services;

import mb.booksy.domain.order.delivery.Delivery;
import mb.booksy.web.model.CourierDeliveryDto;
import mb.booksy.web.model.DeliveryPointDto;
import mb.booksy.web.model.InpostBoxDto;
import java.util.List;

public interface DeliveryService extends BaseService<Delivery, Long> {

    void saveInpostDelivery(InpostBoxDto inpostBoxDto);
    void saveCourierDelivery(CourierDeliveryDto courierDeliveryDto);
    void savePointDelivery(DeliveryPointDto deliveryPointDto);
    List<InpostBoxDto> getAllInpostBoxes();
    List<DeliveryPointDto> getAllZabkaPoints();
    List<DeliveryPointDto> getAllRuchPoints();
}
