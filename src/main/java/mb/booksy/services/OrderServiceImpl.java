package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.Complaint;
import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.user.Client;
import mb.booksy.exceptions.AuthException;
import mb.booksy.repository.CartRepository;
import mb.booksy.repository.ComplaintRepository;
import mb.booksy.repository.ItemRepository;
import mb.booksy.repository.OrderRepository;
import mb.booksy.web.mapper.ComplaintMapper;
import mb.booksy.web.mapper.OrderMapper;
import mb.booksy.web.model.ComplaintDto;
import mb.booksy.web.model.OrderDto;
import mb.booksy.web.model.PersonDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserAuthenticationService userAuthenticationService;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ComplaintRepository complaintRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;
    private final ComplaintMapper complaintMapper;
    private final ItemService itemService;

    public OrderServiceImpl(OrderRepository orderRepository, UserAuthenticationService userAuthenticationService, CartRepository cartRepository, ComplaintRepository complaintRepository, ItemRepository itemRepository, OrderMapper orderMapper, ComplaintMapper complaintMapper, ItemService itemService) {
        this.orderRepository = orderRepository;
        this.userAuthenticationService = userAuthenticationService;
        this.cartRepository = cartRepository;
        this.complaintRepository = complaintRepository;
        this.itemRepository = itemRepository;
        this.orderMapper = orderMapper;
        this.complaintMapper = complaintMapper;
        this.itemService = itemService;
    }

    @Override
    @Transactional
    public void saveOrder(PersonDto person) {
        Client client = (Client) userAuthenticationService.getAuthenticatedUser();
        Cart cart = cartRepository.findByCartId(itemService.getCurrentCartId());
        Order order = Order.builder().orderDate(LocalDate.now()).client(client).cart(cart).ifEnded(false).build();

        order.setReceiverName(person.getName());
        order.setReceiverSurname(person.getSurname());
        order.setReceiverMail(person.getEmail());
        order.setReceiverPhone(person.getPhone());

        orderRepository.save(order);

    }

    @Override
    public boolean validateLogin(PersonDto personDto) {
        Client client = (Client)userAuthenticationService.getAuthenticatedUser();
        if(personDto.getLogin() != null && personDto.getPassword() != null)
            if(client.getLogin().equals(personDto.getLogin()))
                return true;
        return false;
    }

    @Override
    public List<OrderDto> findAllUserOrders(Long... client) {
        try {
            Long clientId;
            if(client.length == 0)
                clientId = userAuthenticationService.getAuthenticatedClientId();
            else
                clientId = client[0];

            return orderRepository.findAllUserOrders(clientId)
                    .stream()
                    .map(order -> orderMapper.orderToOrderDto(order))
                    .map(order -> {
                        order.setProductNumber(orderRepository.countOrderItems(order.getId()));
                        double newPrice = orderRepository.countAmount(order.getId()) * 90;
                        newPrice = Math.round(newPrice);
                        newPrice /= 100;
                        order.setAmount(newPrice);
                        return order;
                    })
                    .collect(Collectors.toList());
        } catch(NullPointerException e) {
           throw new AuthException("User unauthorized");
        }
    }

    @Override
    public OrderDto findOrderById(String orderId) {
        Order order = orderRepository.findOrderById(Long.valueOf(orderId));
        OrderDto orderDto = orderMapper.orderToOrderDto(order);
        orderDto.setProductNumber(orderRepository.countOrderItems(Long.valueOf(orderId)));
        return orderDto;
    }

    @Override
    public boolean validateComplaint(String orderId, ComplaintDto complaintDto) {
        if(complaintDto.getCompItem() != null && complaintDto.getCompReason() != null && complaintDto.getCompMethod() != null) {
            Complaint complaint = complaintMapper.complaintDtoToComplaint(complaintDto);
            Order order = orderRepository.findOrderById(Long.valueOf(orderId));
            if(order != null) {
                complaint.setOrder(order);
                complaintRepository.save(complaint);
                return true;
            } else
                return false;
        }
        return false;
    }

    @Override
    public List<ComplaintDto> findAllUserComplaints() {
        Long clientId = userAuthenticationService.getAuthenticatedClientId();
        List<ComplaintDto> complaints = complaintRepository.findAllByUserId(clientId)
                .stream()
                .map(complaint -> {
                    ComplaintDto dto = complaintMapper.complaintToComplaintDto(complaint);
                    Item item = itemRepository.findByItemId(complaint.getItemId());
                    dto.setCompItemName(item.getItemName());
                    if(complaint.getAcceptance() == true)
                        dto.setAcceptance("TAK");
                    else
                        dto.setAcceptance("NIE");
                    return dto;
                })
                .collect(Collectors.toList());
        return complaints;
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
    public void delete(Order object) {}

    @Override
    public void deleteById(Long aLong) {}
}
