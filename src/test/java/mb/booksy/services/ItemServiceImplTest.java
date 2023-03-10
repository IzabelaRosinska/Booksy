//package mb.booksy.services;
//
//import mb.booksy.domain.inventory.Item;
//import mb.booksy.domain.order.Order;
//import mb.booksy.domain.order.cart.Cart;
//import mb.booksy.domain.order.cart.ItemInCart;
//import mb.booksy.domain.user.Client;
//import mb.booksy.repository.CartRepository;
//import mb.booksy.repository.ItemRepository;
//import mb.booksy.repository.OrderRepository;
//import mb.booksy.web.mapper.ItemMapper;
//import mb.booksy.web.model.ItemDto;
//import mb.booksy.web.model.OrderDto;
//import org.junit.jupiter.api.*;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.util.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import mb.booksy.exceptions.AuthException;
//import mb.booksy.repository.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithUserDetails;
//import java.util.ArrayList;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//
//@SpringBootTest
//class ItemServiceImplTest {
//
//    @Mock
//    ItemRepository itemRepository;
//
//    @Mock
//    CartRepository cartRepository;
//
//    @Mock
//    ItemInCartRepository itemInCartRepository;
//
//    @Mock
//    OrderRepository orderRepository;
//
//    @Mock
//    ItemMapper itemMapper;
//
//    @InjectMocks
//    ItemServiceImpl itemService;
//
//    private Client clientA;
//    private Client clientB;
//
//    private ItemDto itemADto;
//    private Item itemA;
//
//    private Cart cartA;
//    private Cart cartB;
//    private ItemInCart itemInCart;
//
//    private static List<ItemInCart> itemInCartList;
//    private static List<Cart> cartList;
//
//    @BeforeAll
//    static void generalSetUp() {
//        itemInCartList = new ArrayList<>();
//        cartList = new ArrayList<>();
//    }
//
//    @BeforeEach
//    void setUp() {
//        clientA = Client.builder().id(1L).name("ClientA").email("AC@wp.pl").login("AC").build();
//        clientB = Client.builder().id(2L).name("ClientB").email("BC@wp.pl").login("BC").build();
//
//        itemA = Item.builder().id(1L).price(4.99).itemImage(new byte[1]).build();
//        itemADto = new ItemDto();
//        itemADto.setId(1L);
//
//        cartA = Cart.builder().id(1L).build();
//        cartB = Cart.builder().id(2L).build();
//        cartList.add(cartA);
//
//        itemInCart = ItemInCart.builder().id(1L).cart(cartA).item(itemA).number(2).build();
//        itemInCartList.add(itemInCart);
//    }
//
//    @DisplayName("Find all items attached to order - status Ok")
//    @Test
//    void findAllOrderItemTest() {
//        // given
//        List<Item> itemList = new ArrayList<>();
//        itemList.add(itemA);
//
//        // when
//        when(itemRepository.findAllInOrder(anyString())).thenReturn(itemList);
//        when(itemMapper.itemToItemDto(itemA)).thenReturn(itemADto);
//        when(orderRepository.findCartIdForOrder(anyLong())).thenReturn(1L);
//        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(itemA));
//        when(itemInCartRepository.findItemInCart(anyLong(), anyLong())).thenReturn(itemInCartList);
//        List<ItemDto> items = itemService.findAllOrderItem("1");
//
//        // then
//        assertEquals(items.size(), 1);
//        verify(itemRepository).findAllInOrder(anyString());
//    }
//
//    @DisplayName("Find all items attached to order - order doesn't exist")
//    @Test
//    void findAllOrderNotExistItemTest() {
//        // when
//        when(itemRepository.findAllInOrder(anyString())).thenReturn(new ArrayList<>());
//        when(itemMapper.itemToItemDto(itemA)).thenReturn(itemADto);
//        when(orderRepository.findCartIdForOrder(anyLong())).thenReturn(1L);
//        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(itemA));
//        when(itemInCartRepository.findItemInCart(anyLong(), anyLong())).thenReturn(itemInCartList);
//        List<ItemDto> items = itemService.findAllOrderItem("1");
//
//        // then
//        assertEquals(items.size(), 0);
//        verify(itemRepository).findAllInOrder(anyString());
//    }
//
//    @DisplayName("Get current cart for authenticated user - user is unauthorized")
//    @Test
//    void getCurrentCartIdUnauthorizedUserTest() {
//        // given + when
//        Exception userUnauthorizedException = assertThrows(AuthException.class, () -> {
//            itemService.getCurrentCartId();
//        }, "User unauthorized");
//
//        // then
//        assertEquals(AuthException.class, userUnauthorizedException.getClass());
//    }
//
//    @WithUserDetails("AC@wp.pl")
//    @DisplayName("Get current cart for authenticated user - user doesn't have any carts")
//    @Test
//    void getCurrentCartIdNoCartsTest() {
//        // given
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Client person = (Client)authentication.getPrincipal();
//
//        // when
//        when(cartRepository.findClCarts(anyLong())).thenReturn(new ArrayList<>());
//        when(orderRepository.findOrderWithCartId(anyLong())).thenReturn(null);
//        Long id = itemService.getCurrentCartId(person.getId());
//
//        // then
//        assertEquals(id, -1);
//        verify(cartRepository).findClCarts(anyLong());
//    }
//
//    @WithUserDetails("AC@wp.pl")
//    @DisplayName("Get current cart for authenticated user - no order attached for cart")
//    @Test
//    void getCurrentCartIdNoOrderTest() {
//        // given
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Client person = (Client)authentication.getPrincipal();
//        List<Cart> cartListNoOrder = new ArrayList<>();
//        cartListNoOrder.add(cartA);
//
//        // when
//        when(cartRepository.findClCarts(anyLong())).thenReturn(cartListNoOrder);
//        when(orderRepository.findOrderWithCartId(anyLong())).thenReturn(null);
//        Long id = itemService.getCurrentCartId(person.getId());
//
//        // then
//        assertEquals(id, 1);
//        verify(cartRepository).findClCarts(anyLong());
//        verify(orderRepository).findOrderWithCartId(anyLong());
//    }
//
//    @WithUserDetails("AC@wp.pl")
//    @DisplayName("Get current cart for authenticated user - all carts closed")
//    @Test
//    void getCurrentCartIdAllClosedTest() {
//        // given
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Client person = (Client)authentication.getPrincipal();
//        Order orderA = Order.builder().id(1L).client(clientA).cart(null).ifEnded(true).build();;
//
//        // when
//        when(cartRepository.findClCarts(anyLong())).thenReturn(cartList);
//        when(orderRepository.findOrderWithCartId(anyLong())).thenReturn(orderA);
//        Long id = itemService.getCurrentCartId(person.getId());
//
//        // then
//        assertEquals(id, -1L);
//        verify(cartRepository).findClCarts(anyLong());
//        verify(orderRepository).findOrderWithCartId(anyLong());
//    }
//
//    @WithUserDetails("AC@wp.pl")
//    @DisplayName("Get current cart for authenticated user - cart opened, order attached")
//    @Test
//    void getCurrentCartIdCartOpenedTest() {
//        // given
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Client person = (Client)authentication.getPrincipal();
//        Order orderB = Order.builder().id(2L).client(clientB).cart(cartB).ifEnded(false).build();
//        List<Cart> cartListNotEnded = new ArrayList<>();
//        cartListNotEnded.add(cartB);
//
//        // when
//        when(cartRepository.findClCarts(anyLong())).thenReturn(cartListNotEnded);
//        when(orderRepository.findOrderWithCartId(anyLong())).thenReturn(orderB);
//        Long id = itemService.getCurrentCartId(person.getId());
//
//        // then
//        assertEquals(id, 2);
//        verify(cartRepository).findClCarts(anyLong());
//        verify(orderRepository).findOrderWithCartId(anyLong());
//    }
//}