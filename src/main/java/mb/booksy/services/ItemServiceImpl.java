package mb.booksy.services;

import mb.booksy.domain.inventory.AvailabilityAlert;
import mb.booksy.domain.inventory.Favorite;
import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.ItemInReturn;
import mb.booksy.domain.order.Order;
import mb.booksy.domain.order.OrderReturn;
import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.order.cart.ItemInCart;
import mb.booksy.domain.user.Client;
import mb.booksy.exceptions.AuthException;
import mb.booksy.repository.*;
import mb.booksy.web.mapper.ItemMapper;
import mb.booksy.web.model.ItemDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ItemInCartRepository itemInCartRepository;
    private final ItemMapper itemMapper;
    private final UserAuthenticationService userAuthenticationService;
    private final ClientRepository clientRepository;
    private final FavoriteRepository favoriteRepository;

    private final AvailabilityAlertRepository availabilityAlertRepository;

    private final OrderReturnRepository orderReturnRepository;
    private final ItemInReturnRepository itemInReturnRepository;

    public ItemServiceImpl(ItemRepository itemRepository, CartRepository cartRepository, OrderRepository orderRepository, ItemInCartRepository itemInCartRepository, ItemMapper itemMapper, UserAuthenticationService userAuthenticationService, ClientRepository clientRepository, FavoriteRepository favoriteRepository, AvailabilityAlertRepository availabilityAlertRepository, OrderReturnRepository orderReturnRepository, ItemInReturnRepository itemInReturnRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.itemInCartRepository = itemInCartRepository;
        this.itemMapper = itemMapper;
        this.userAuthenticationService = userAuthenticationService;
        this.clientRepository = clientRepository;
        this.favoriteRepository = favoriteRepository;
        this.availabilityAlertRepository = availabilityAlertRepository;
        this.orderReturnRepository = orderReturnRepository;
        this.itemInReturnRepository = itemInReturnRepository;
    }

    @Override
    public Set<Item> findAll() {
        Set<Item> items = new HashSet<>();
        itemRepository.findAll().forEach(items::add);
        return items;
    }

    @Override
    public List<ItemDto> findAllItem() {
        List<ItemDto> items = itemRepository.findAll()
                .stream()
                .map(item -> itemMapper.itemToItemDtoFull(item))
                .collect(Collectors.toList());

        return items;
    }

    @Override
    public ItemDto findByItemId(Long itemId) {
        return itemMapper.itemToItemDtoFull(itemRepository.findByItemId(itemId));
    }

    @Override
    public List<ItemDto> findAllCartItem() {
        Client client = (Client)userAuthenticationService.getAuthenticatedUser();
        List<Cart> carts_list = cartRepository.findClCarts(client.getId());
        List<Cart> carts = new ArrayList<>();

        if(carts_list.size() != 0)
            for(int i = 0; i < carts_list.size(); i++) {
                Order orders = orderRepository.findOrderWithCartId(carts_list.get(i).getId());
                if((orders == null) || (orders.getIfEnded() == false))
                    carts.add(carts_list.get(i));
            }

        List<ItemDto> itemsInCart = null;
        if(carts.size() == 0) {
            Cart newCart = Cart.builder().client(client).initDate(LocalDate.now()).itemNumber(0).build();
            cartRepository.save(newCart);
            itemsInCart = new ArrayList<>();
        } else {
            Long cartId = carts.get(0).getId();
            itemsInCart = itemRepository.findAllInCart(cartId)
                    .stream()
                    .map(item -> getItemDetails(itemMapper.itemToItemDto(item), cartId))
                    .collect(Collectors.toList());
        }
        return itemsInCart;
    }

    private ItemDto getItemDetails(ItemDto itemDto, Long cartId){
        String itemPhoto = "";
        try{
            Item item = itemRepository.findById(itemDto.getId()).get();
            itemPhoto = new String(Base64.encodeBase64(item.getItemImage()), "UTF-8");
        }catch(IOException e) {
            e.printStackTrace();
        }
        itemDto.setImage(itemPhoto);
        int amount = itemInCartRepository.findItemInCart(itemDto.getId(), cartId).get(0).getNumber();
        double price = amount * itemRepository.findById(itemDto.getId()).get().getPrice();
        itemDto.setCartAmount(amount);
        itemDto.setCartPrice(price);
        return itemDto;
    }

    @Override
    public Double countPrice() {
        Long id = getCurrentCartId();
        if(id != -1L && cartRepository.findCartSize(id) != 0)
            return (itemInCartRepository.countCartPrice(id) - cartRepository.findByCartId(id).getPoints());
        return 0.0;
    }

    @Override
    public Double countDiscount() {
        Long id = getCurrentCartId();
        if(id != -1L && cartRepository.findCartSize(id) != 0) {
            double newPrice = itemInCartRepository.countCartDiscount(id);
            newPrice = Math.round(newPrice);
            newPrice /= 100;
            return (newPrice - cartRepository.findByCartId(id).getPoints());
        } else {
            return 0.0;
        }
    }

    @Override
    public List<String> findDeliveryImage() {
        List<String> images = new ArrayList<>();
        images.add(createImage("src/main/resources/static/resources/images/box.jpg"));
        images.add(createImage("src/main/resources/static/resources/images/dhl.jpg"));
        images.add(createImage("src/main/resources/static/resources/images/zabka.jpg"));
        images.add(createImage("src/main/resources/static/resources/images/ruch.jpg"));
        return images;
    }

    private String createImage(String path) {
        byte[] array = null;
        String itemPhoto = "";
        try {
            array = Files.readAllBytes(Paths.get(path));
            itemPhoto = new String(Base64.encodeBase64(array), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemPhoto;
    }

    @Override
    public List<ItemDto> findAllOrderItem(String orderId) {
        List<ItemDto> itemsInOrder = itemRepository.findAllInOrder(orderId)
                .stream()
                .map(item -> getItemDetails(itemMapper.itemToItemDto(item), orderRepository.findCartIdForOrder(Long.valueOf(orderId))))
                .collect(Collectors.toList());

        return itemsInOrder;
    }

    @Override
    public List<ItemDto> findAllOrderReturnItem(Long orderReturnId) {
        List<ItemDto> itemsInOrderReturn = new ArrayList<>();
        List<ItemInReturn> itemsInReturnIds = itemInReturnRepository.findItemsInOrderReturn(orderReturnId);

        for(ItemInReturn itemInReturn : itemsInReturnIds) {
            ItemDto newItem = itemMapper.itemToItemDtoFull(findById(itemInReturn.getItem().getId()));
            newItem.setNumberInOrderReturn(itemInReturn.getNumber());
            itemsInOrderReturn.add(newItem);
        }

        return itemsInOrderReturn;
    }

    @Override
    public List<ItemDto> orderReturnItemAddNumberFromCart(List<ItemDto> itemsInOrderReturn,
                                                List<ItemDto> itemsInOrder) {

//        for(ItemDto itemInReturn : itemsInOrderReturn) {
//            for(ItemDto itemInOrder: itemsInOrder) {
//                itemInReturn.setCartAmount(itemInOrder.getCartAmount());
//            }
//        }

        for(int i = 0; i < itemsInOrderReturn.size(); i++) {
            for(ItemDto itemInOrder: itemsInOrder) {
                if (itemsInOrderReturn.get(i).getId() == itemInOrder.getId()) {
                    itemsInOrderReturn.get(i).setCartAmount(itemInOrder.getCartAmount());
                }
            }
        }

        return itemsInOrderReturn;
    }

    @Override
    @Transactional
    public void deleteItemInOrderReturn(Long orderReturnId, Long itemId) {
        itemInReturnRepository.deleteItemInOrderReturn(orderReturnId, itemId);
    }

    @Override
    public Integer countItemInOrderReturn(Long orderReturnId) {
        return itemInReturnRepository.countItemsInOrderReturn(orderReturnId);
    }

    @Override
    @Transactional
    public String updateItemNumber(Integer newNumber, Integer maxNumber, Long orderReturnId, Long itemId) {

        if(newNumber > maxNumber)
            return "Liczba dostępnych sztuk - " + maxNumber;
        else if(newNumber < 1)
            return "Liczba sztuk musi być większa od 0";
        else {
            itemInReturnRepository.updateItemNumber(newNumber, orderReturnId, itemId);
            return "";
        }
    }

    @Override
    @Transactional
    public Long getCurrentCartId(Long... client) {
        Long clientId;
        try {
            if (client.length == 0)
                clientId = userAuthenticationService.getAuthenticatedClientId();
            else
                clientId = client[0];
        } catch(NullPointerException e) {
            throw new AuthException("User unauthorized");
        }

        List<Cart> carts_list = cartRepository.findClCarts(clientId);
        List<Cart> carts = new ArrayList<>();

        if(carts_list.size() != 0)
            for(int i = 0; i < carts_list.size(); i++) {
                Order orders = orderRepository.findOrderWithCartId(carts_list.get(i).getId());
                if((orders == null) || (orders.getIfEnded() == false))
                    carts.add(carts_list.get(i));
            }

        if(carts.size() == 0)
            return -1L;
        return carts.get(0).getId();
    }

    @Override
    public Long provideCart() {
        Client client = (Client)userAuthenticationService.getAuthenticatedUser();
        List<Cart> carts_list = cartRepository.findClCarts(client.getId());
        List<Cart> carts = new ArrayList<>();

        if(carts_list.size() != 0)
            for(int i = 0; i < carts_list.size(); i++) {
                Order orders = orderRepository.findOrderWithCartId(carts_list.get(i).getId());
                if((orders == null) || (orders.getIfEnded() == false))
                    carts.add(carts_list.get(i));
            }

        if(carts.size() == 0) {
            Cart newCart = Cart.builder().client(client).initDate(LocalDate.now()).itemNumber(0).build();
            cartRepository.save(newCart);

            return newCart.getId();
        }

        return carts.get(0).getId();
    }

    @Override
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    @Override
    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void delete(Item item) {
        itemRepository.delete(item);
    }

    @Override
    public void deleteById(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public boolean isInCart(Long itemId) {
        Long cartId = getCurrentCartId();

        if (cartId == -1) {
            return false;
        }

        if (!itemInCartRepository.findItemInCart(itemId, cartId).isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isFavorite(Long itemId) {
        Long clientId = (userAuthenticationService.getAuthenticatedUser()).getId();
        Item item = itemRepository.findByItemId(itemId);

        return favoriteRepository.findClientFavorites(clientId).contains(item.getId());
    }

    @Override
    public void addToFavorites(Long itemId) {
        if (!isFavorite(itemId)) {
            favoriteRepository.save(Favorite.builder().clientId(((Client)userAuthenticationService.getAuthenticatedUser())).itemId(itemRepository.findByItemId(itemId)).build());
        }
    }

    @Override
    public void deleteFromFavorites(Long itemId) {
        if (isFavorite(itemId)) {
            favoriteRepository.deleteClientFavoriteItem(itemId, ((Client)userAuthenticationService.getAuthenticatedUser()).getId());
        }
    }

    @Override
    public void addAlert(String mail, Long itemId) {
        mail = mail.replace(",", "");
        availabilityAlertRepository.save(new AvailabilityAlert().builder().mail(mail).item(itemRepository.findByItemId(itemId)).build());
    }
}
