package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.cart.Cart;

public interface CartService  extends BaseService<Cart, Long> {

    void deleteItemFromCart(Long cartId, Long itemId);
    String updateItemNumber(Long cartId, Long itemId, Integer new_number);
}
