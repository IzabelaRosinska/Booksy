package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.web.model.ItemDto;
import java.util.List;

public interface ItemService extends BaseService<Item, Long> {

    List<ItemDto> findAllCartItem(Long cartId);

    Double countPrice(Long cartId);

    Double countDiscount(Long cartId);

    List<String> findDeliveryImage();

    List<ItemDto> findAllOrderItem(String orderId, Long cartId);
}
