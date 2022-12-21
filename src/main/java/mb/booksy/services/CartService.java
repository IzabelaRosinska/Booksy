package mb.booksy.services;

import mb.booksy.domain.order.cart.Cart;
import mb.booksy.web.model.MethodDto;
import mb.booksy.web.model.ReasonDto;
import java.util.List;

public interface CartService  extends BaseService<Cart, Long> {

    void deleteItemFromCart(Long cartId, Long itemId);

    String updateItemNumber(Long cartId, Long itemId, Integer newNumber);

    List<ReasonDto> getComplaintReasons();

    List<MethodDto> getComplaintMethods();
}
