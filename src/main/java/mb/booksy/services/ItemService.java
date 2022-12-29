package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.user.Client;
import mb.booksy.web.model.ItemDto;
import java.util.List;

public interface ItemService extends BaseService<Item, Long> {

    Double countPrice();
    Double countDiscount();
    Long getCurrentCartId(Long... client);

    List<String> findDeliveryImage();

    List<ItemDto> findAllCartItem();
    List<ItemDto> findAllOrderItem(String orderId);
}
