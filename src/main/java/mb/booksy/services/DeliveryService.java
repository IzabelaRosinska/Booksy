package mb.booksy.services;

import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.order.delivery.Delivery;
import mb.booksy.domain.order.delivery.InpostBox;
import mb.booksy.web.model.DeliveryPointDto;
import mb.booksy.web.model.InpostBoxDto;

import java.util.List;

public interface DeliveryService extends BaseService<Delivery, Long> {

    List<InpostBoxDto> getAllInpostBoxes();
    List<DeliveryPointDto> getAllZabkaPoints();
    List<DeliveryPointDto> getAllRuchPoints();
}
