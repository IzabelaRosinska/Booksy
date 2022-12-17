package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.web.model.ItemDto;
import java.util.List;

public interface ItemService extends BaseService<Item, Long> {

    List<ItemDto> findAllCartItem(Long cartId);

    Double countPrice(Long cartId);

}
