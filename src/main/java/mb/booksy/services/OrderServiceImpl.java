package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.Complaint;
import mb.booksy.domain.order.Order;
import mb.booksy.domain.user.Client;
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
    private final OrderMapper mapper;
    private final ComplaintMapper complaintMapper;


    public OrderServiceImpl(OrderRepository orderRepository, UserAuthenticationService userAuthenticationService, CartRepository cartRepository, ComplaintRepository complaintRepository, ItemRepository itemRepository, OrderMapper mapper, ComplaintMapper complaintMapper) {
        this.orderRepository = orderRepository;
        this.userAuthenticationService = userAuthenticationService;
        this.cartRepository = cartRepository;
        this.complaintRepository = complaintRepository;
        this.itemRepository = itemRepository;
        this.mapper = mapper;
        this.complaintMapper = complaintMapper;

    }

    @Override
    @Transactional
    public void saveOrder(PersonDto person) {
        List<Order> orders = orderRepository.findLastIndex();
        Long lastId = 1L;
        if(orders.size() != 0)
            lastId = orderRepository.findLastIndex().get(0).getId() + 1;

        Order order = Order.builder().id(lastId).build();
        order.setOrderDate(LocalDate.now());
        order.setClient((Client)userAuthenticationService.getAuthenticatedUser());
        order.setCart(cartRepository.findByCartId(userAuthenticationService.getAuthenticatedUser().getId()));
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
    public List<OrderDto> findAllUserOrders() {
        Long clientId = userAuthenticationService.getAuthenticatedClientId();
        List<OrderDto> orders = orderRepository.findAllUserOrders(clientId).stream()
                .map(order -> mapper.orderToOrderDto(order))
                .map(order -> {
                    order.setProductNumber(orderRepository.countOrderItems(order.getId()));
                    order.setAmount(orderRepository.countAmount(order.getId()));
                    return order;
                })
                .collect(Collectors.toList());
        return orders;
    }

    @Override
    public OrderDto findOrderById(String orderId) {
        Order order = orderRepository.findOrderById(Long.valueOf(orderId));
        OrderDto orderDto = mapper.orderToOrderDto(order);
        orderDto.setProductNumber(orderRepository.countOrderItems(Long.valueOf(orderId)));
        return orderDto;
    }

    @Override
    public boolean validateComplaint(String orderId, ComplaintDto complaintDto) {
        if(complaintDto.getCompItem() != null && complaintDto.getCompReason() != null && complaintDto.getCompMethod() != null) {
            Complaint complaint = complaintMapper.complaintDtoToComplaint(complaintDto);
            complaint.setOrder(orderRepository.findOrderById(Long.valueOf(orderId)));
            complaintRepository.save(complaint);
            return true;
        }
        return false;
    }

    @Override
    public List<ComplaintDto> findAllUserComplaints(Long authenticatedClientId) {
        List<ComplaintDto> complaints = complaintRepository.findAllByUserId(authenticatedClientId)
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
    public void delete(Order object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
