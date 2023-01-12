package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.user.Client;
import mb.booksy.web.model.ItemDto;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemService extends BaseService<Item, Long> {

    Double countPrice();
    Double countDiscount();
    Long getCurrentCartId(Long... client);

    List<String> findDeliveryImage();

    List<ItemDto> findAllCartItem();
    List<ItemDto> findAllOrderItem(String orderId);

    List<ItemDto> findAllItem();

    ItemDto findByItemId(Long itemId);

    Long provideCart();

    boolean isInCart(Long itemId);

    boolean isFavorite(Long itemId);

    void addToFavorites(Long itemId);

    void deleteFromFavorites(Long itemId);

    void addAlert(String mail, Long itemId);

    List<ItemDto> findAllOrderReturnItem(Long orderReturnId);
    List<ItemDto> orderReturnItemAddNumberFromCart(List<ItemDto> itemsInOrderReturn, List<ItemDto> itemsInOrder);

    void deleteItemInOrderReturn(Long orderReturnId, Long itemId);

    Integer countItemInOrderReturn(Long orderReturnId);

    String updateItemNumber(Integer newNumber, Integer maxNumber, Long orderReturnId, Long itemId);

}
