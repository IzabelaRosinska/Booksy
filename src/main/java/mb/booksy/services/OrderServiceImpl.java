package mb.booksy.services;

import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.user.Client;
import mb.booksy.repository.CartRepository;
import mb.booksy.repository.OrderRepository;
import mb.booksy.web.model.PersonDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserAuthenticationService userAuthenticationService;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserAuthenticationService userAuthenticationService, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.userAuthenticationService = userAuthenticationService;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public void saveOrder(PersonDto person) {
        List<Order> orders = orderRepository.findLastIndex();
        Long lastId = 1L;
        if(orders.size() != 0)
            lastId = orderRepository.findLastIndex().get(0).getId() + 1;
        Order order = Order.builder().id(lastId).build();

        order.setClient((Client)userAuthenticationService.getAuthenticatedUser());
        order.setCart(cartRepository.findByCartId(userAuthenticationService.getAuthenticatedUser().getId()));
        order.setReceiverName(person.getName());
        order.setReceiverSurname(person.getSurname());
        order.setReceiverMail(person.getEmail());
        order.setReceiverPhone(person.getPhone());

        orderRepository.save(order);
    }

    @Override
    public Set<Order> findAll() {
        return null;
    }

    @Override
    public Order findById(Long aLong) {
        return null;
    }

    @Override
    public Order save(Order object) {
        return null;
    }

    @Override
    public void delete(Order object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
