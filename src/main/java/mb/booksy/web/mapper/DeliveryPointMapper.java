package mb.booksy.web.mapper;

import mb.booksy.domain.order.delivery.DeliveryPoint;
import mb.booksy.web.model.DeliveryPointDto;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPointMapper {

    public DeliveryPointDto pointToPointDto(DeliveryPoint point) {
        DeliveryPointDto pointDto = new DeliveryPointDto();
        pointDto.setId(point.getId());
        pointDto.setPointAddress(point.getAddressName());
        return pointDto;
    }
}
