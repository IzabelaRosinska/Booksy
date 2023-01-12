package mb.booksy.services;

import mb.booksy.domain.order.Order;
import mb.booksy.web.model.*;

import java.util.List;

public interface OrderService extends BaseService<Order, Long> {

    void saveOrder(PersonDto person);

    boolean validateLogin(PersonDto personDto);
    boolean validateComplaint(String orderId, ComplaintDto complaintDto);

    List<OrderDto> findAllUserOrders(Long... client);
    List<ComplaintDto> findAllUserComplaints();

    OrderDto findOrderById(String orderId);

    boolean validateItemInReturn(String orderReturnId, ItemInReturnDto itemInReturnDto);

    Long createNewOrderReturn(String orderId);
    boolean validOrderReturn(String orderReturnId);

    void deleteOrderReturn(String orderReturnId);

    List<OrderReturnDto> findAllUserOrderReturns();

    OrderReturnDto findOrderReturnById(String orderReturnId);
}
