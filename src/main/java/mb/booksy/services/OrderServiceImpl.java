package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.Complaint;
import mb.booksy.domain.order.ItemInReturn;
import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.OrderReturn;
import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.user.Client;
import mb.booksy.exceptions.AuthException;
import mb.booksy.repository.*;
import mb.booksy.web.mapper.ComplaintMapper;
import mb.booksy.web.mapper.OrderMapper;
import mb.booksy.web.mapper.OrderReturnMapper;
import mb.booksy.web.model.*;
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
    private final OrderReturnMapper orderReturnMapper;
    private final ItemService itemService;
    private final ItemInReturnRepository itemInReturnRepository;
    private final OrderReturnRepository orderReturnRepository;

    public OrderServiceImpl(UserAuthenticationService userAuthenticationService, OrderRepository orderRepository, CartRepository cartRepository, ComplaintRepository complaintRepository, ItemRepository itemRepository, OrderMapper orderMapper, ComplaintMapper complaintMapper, OrderReturnMapper orderReturnMapper, ItemService itemService, ItemInReturnRepository itemInReturnRepository, OrderReturnRepository orderReturnRepository) {
        this.userAuthenticationService = userAuthenticationService;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.complaintRepository = complaintRepository;
        this.itemRepository = itemRepository;
        this.orderMapper = orderMapper;
        this.complaintMapper = complaintMapper;
        this.orderReturnMapper = orderReturnMapper;
        this.itemService = itemService;
        this.itemInReturnRepository = itemInReturnRepository;
        this.orderReturnRepository = orderReturnRepository;
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
        if(complaintDto.getCompItem() != 0 && complaintDto.getCompReason() != "" && complaintDto.getCompMethod() != "") {
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
    public boolean validateItemInReturn(String orderReturnId, ItemInReturnDto itemInReturnDto) {
        if(itemInReturnDto.getItemId() != 0) {
            if(orderReturnId != null && orderReturnId != "") {
                if (itemInReturnRepository.findItemInOrderReturn(itemInReturnDto.getItemId(), Long.valueOf(orderReturnId)).isEmpty()) {
                    itemInReturnRepository.save(ItemInReturn.builder()
                            .item(itemRepository.findByItemId(itemInReturnDto.getItemId()))
                            .orderReturn(orderReturnRepository
                                    .findByOrderReturnId(Long.valueOf(orderReturnId)))
                            .number(1).build());
                }

                return true;
            } else
                return false;
        }

        return false;
    }

    @Override
    public Long createNewOrderReturn(String orderId) {
        List<OrderReturn> orderReturns = orderReturnRepository.findByOrderId(Long.valueOf(orderId));

        if (!orderReturns.isEmpty()) {
            for (OrderReturn orderReturn : orderReturns) {
                if (!orderReturn.getIsValid()) {
                    return orderReturn.getId();
                }
            }
        }

        OrderReturn newOrderReturn = OrderReturn.builder().acceptance(false).initDate(LocalDate.now()).build();
        newOrderReturn.setIsValid(false);
        newOrderReturn.setOrder(orderRepository.findOrderById(Long.valueOf(orderId)));
        orderReturnRepository.save(newOrderReturn);

        return newOrderReturn.getId();
    }

    @Override
    @Transactional
    public void deleteOrderReturn(String orderReturnId) {
        itemInReturnRepository.deleteOrderReturnItems(Long.valueOf(orderReturnId));
        orderReturnRepository.deleteOrderReturn(Long.valueOf(orderReturnId));
    }

    @Override
    @Transactional
    public boolean validOrderReturn(String orderReturnId) {
        Integer itemNumber = itemInReturnRepository.countItemsInOrderReturn(Long.valueOf(orderReturnId));

        if (itemNumber > 0)
        {
            orderReturnRepository.validateOrderReturn(Long.valueOf(orderReturnId));
            return true;
        }
        else {
            return false;
        }
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
    public List<OrderReturnDto> findAllUserOrderReturns() {
        List<OrderReturnDto> orderReturnDtos = orderReturnRepository.findAllByUserId(userAuthenticationService.getAuthenticatedClientId())
                .stream()
                .map(orderReturn -> orderReturnMapper.orderReturnToOrderReturnDto(orderReturn))
                .collect(Collectors.toList());;

        return orderReturnDtos;
    }

    @Override
    public OrderReturnDto findOrderReturnById(String orderReturnId) {
        return orderReturnMapper.orderReturnToOrderReturnDto(orderReturnRepository.findByOrderReturnId(Long.valueOf(orderReturnId)));
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
