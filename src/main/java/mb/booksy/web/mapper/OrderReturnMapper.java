package mb.booksy.web.mapper;

import mb.booksy.domain.order.Complaint;
import mb.booksy.domain.order.OrderReturn;
import mb.booksy.web.model.ComplaintDto;
import mb.booksy.web.model.OrderReturnDto;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderReturnMapper {

    public OrderReturnDto orderReturnToOrderReturnDto(OrderReturn orderReturn) {
        OrderReturnDto orderReturnDto = new OrderReturnDto();
        orderReturnDto.setReturnId(orderReturn.getId());
        orderReturnDto.setOrderId(orderReturn.getOrder().getId());
        orderReturnDto.setInitDate(orderReturn.getInitDate());

        if (orderReturn.getAcceptance()) {
            orderReturnDto.setAcceptance("TAK");
        }
        else {
            orderReturnDto.setAcceptance("NIE");
        }

        return orderReturnDto;
    }
}
