package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.Complaint;
import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.order.cart.ItemInCart;
import mb.booksy.domain.user.Client;
import mb.booksy.exceptions.AuthException;
import mb.booksy.repository.*;
import mb.booksy.web.mapper.ComplaintMapper;
import mb.booksy.web.mapper.OrderMapper;
import mb.booksy.web.model.ComplaintDto;
import mb.booksy.web.model.OrderDto;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceImplTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    ComplaintRepository complaintRepository;

    @Mock
    OrderMapper orderMapper;

    @Mock
    ComplaintMapper complaintMapper;

    @InjectMocks
    OrderServiceImpl orderService;

    private Client clientA;
    private Client clientB;
    private Order orderA;
    private OrderDto orderADto;
    private Item itemA;
    private Cart cartA;
    private Double amount = 4.99;
    private ItemInCart itemInCart;
    private ComplaintDto complaintDto;
    private Complaint complaint;
    private static List<Order> emptyList;
    private static List<Order> orderList;

    @BeforeAll
    static void generalSetUp() {
        emptyList = new ArrayList<>();
        orderList = new ArrayList<>();
    }

    @AfterAll
    static void generalTearDown() {
    }

    @BeforeEach
    void setUp() {
        clientA = Client.builder().id(1L).name("ClientA").email("AC@wp.pl").login("AC").build();
        clientB = Client.builder().id(2L).name("ClientB").email("BC@wp.pl").login("BC").build();
        cartA = Cart.builder().id(1L).build();
        itemA = Item.builder().id(1L).build();
        itemInCart = ItemInCart.builder().id(1L).cart(cartA).item(itemA).build();
        orderA = Order.builder().id(1L).client(clientA).cart(cartA).build();
        orderADto = new OrderDto();
        orderADto.setId(1L);
        orderList.add(orderA);
    }

    @AfterEach
    void tearDown() {
        amount = 4.99;
    }

    @DisplayName("Find all orders attached to user - user is unauthorized")
    @Test
    void findAllUserOrdersUnauthorizedUserTest() {
        // given + when
        Exception userUnauthorizedException = assertThrows(AuthException.class, () -> {
            orderService.findAllUserOrders();
        }, "User unauthorized");

        // then
        assertEquals(AuthException.class, userUnauthorizedException.getClass());
    }

    @WithUserDetails("CC@wp.pl")
    @DisplayName("Find all orders attached to user - no orders")
    @Test
    void findAllUserOrdersNoOrdersTest() {
        // given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client person = (Client)authentication.getPrincipal();

        // when
        when(orderRepository.findAllUserOrders(anyLong())).thenReturn(emptyList);
        List<OrderDto> orders = orderService.findAllUserOrders(person.getId());

        // then
        assertNotNull(orders);
        assertEquals(orders, emptyList);

        verify(orderRepository).findAllUserOrders(anyLong());
    }

    @WithUserDetails("AC@wp.pl")
    @DisplayName("Find all orders attached to user - only one order")
    @Test
    void findAllUserOrdersOneOrderTest() {
        // given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client person = (Client)authentication.getPrincipal();

        // when
        when(orderRepository.findAllUserOrders(anyLong())).thenReturn(orderList);
        when(orderRepository.countOrderItems(anyLong())).thenReturn(2);
        when(orderRepository.countAmount(anyLong())).thenReturn(amount);
        when(orderMapper.orderToOrderDto(orderA)).thenReturn(orderADto);
        List<OrderDto> testOrders = orderService.findAllUserOrders(person.getId());

        // then
        amount = 4.49;
        assertNotNull(testOrders);
        assertEquals(testOrders.size(), 1);
        assertEquals(testOrders.get(0).getProductNumber(), 2);
        assertEquals(testOrders.get(0).getAmount(), amount);

        verify(orderRepository).findAllUserOrders(anyLong());
        verify(orderRepository).countOrderItems(anyLong());
        verify(orderRepository).countAmount(anyLong());
        verify(orderMapper).orderToOrderDto(anyObject());
    }

    @WithUserDetails("BC@wp.pl")
    @DisplayName("Find all orders attached to user - more than one order")
    @Test
    void findAllUserOrdersManyOrdersTest() {
        // given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client person = (Client)authentication.getPrincipal();
        Order orderB = Order.builder().id(2L).build();
        orderList.add(orderB);

        // when
        when(orderRepository.findAllUserOrders(anyLong())).thenReturn(orderList);
        when(orderRepository.countOrderItems(anyLong())).thenReturn(2);
        when(orderRepository.countAmount(anyLong())).thenReturn(amount);
        when(orderMapper.orderToOrderDto(anyObject())).thenReturn(orderADto);
        List<OrderDto> testOrders = orderService.findAllUserOrders(person.getId());

        // then
        assertNotNull(testOrders);
        assertEquals(testOrders.size(), 2);
    }


    @DisplayName("Validate complaint - incorrect complaint")
    @Test
    void validateComplaintIncorrectComplaintDtoTest() {
        // given
        complaintDto = new ComplaintDto();
        Boolean result = orderService.validateComplaint("1L", complaintDto);
        List<Complaint> complaints = (List<Complaint>) complaintRepository.findAll();

        // then
        assertEquals(result, false);
        assertEquals(complaints.size(), 0);
        verify(complaintRepository).findAll();

    }

    @DisplayName("Validate complaint - incorrect order id")
    @Test
    void validateComplaintNoOrderTest() {
        // given
        complaintDto = new ComplaintDto();
        complaintDto.setCompItem(1L);
        complaintDto.setCompReason("test");
        complaintDto.setCompMethod("test");
        complaint = new Complaint();

        // when
        when(complaintMapper.complaintDtoToComplaint(complaintDto)).thenReturn(complaint);
        when(orderRepository.findOrderById(anyLong())).thenReturn(null);
        Boolean result = orderService.validateComplaint("1", complaintDto);

        // then
        assertEquals(result, false);
        verify(complaintMapper).complaintDtoToComplaint(anyObject());
    }

    @DisplayName("Validate complaint - status ok")
    @Test
    void validateComplaintTest() {
        // given
        complaintDto = new ComplaintDto();
        complaintDto.setCompItem(1L);
        complaintDto.setCompReason("test");
        complaintDto.setCompMethod("test");
        complaint = new Complaint();

        // when
        when(complaintMapper.complaintDtoToComplaint(complaintDto)).thenReturn(complaint);
        when(orderRepository.findOrderById(anyLong())).thenReturn(orderA);
        Boolean result = orderService.validateComplaint("1", complaintDto);

        // then
        assertEquals(result, true);
        verify(complaintMapper).complaintDtoToComplaint(anyObject());
    }
}