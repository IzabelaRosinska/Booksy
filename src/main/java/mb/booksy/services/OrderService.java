package mb.booksy.services;

import mb.booksy.domain.order.Order;
import mb.booksy.web.model.PersonDto;

public interface OrderService extends BaseService<Order, Long> {

    void saveOrder(PersonDto person);

    boolean validateLogin(PersonDto personDto);
}
