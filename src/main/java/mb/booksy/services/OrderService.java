package mb.booksy.services;

import mb.booksy.domain.order.Order;
import mb.booksy.web.model.ComplaintDto;
import mb.booksy.web.model.OrderDto;
import mb.booksy.web.model.PersonDto;
import java.util.List;

public interface OrderService extends BaseService<Order, Long> {

    void saveOrder(PersonDto person);

    boolean validateLogin(PersonDto personDto);
    boolean validateComplaint(String orderId, ComplaintDto complaintDto);

    List<OrderDto> findAllUserOrders();
    List<ComplaintDto> findAllUserComplaints(Long authenticatedClientId);

    OrderDto findOrderById(String orderId);
}
