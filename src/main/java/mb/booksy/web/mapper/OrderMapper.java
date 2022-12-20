package mb.booksy.web.mapper;

import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.payment.Payment;
import mb.booksy.web.model.OrderDto;
import mb.booksy.web.model.PaymentDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto orderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setDate(order.getOrderDate());

        return orderDto;
    }
}
