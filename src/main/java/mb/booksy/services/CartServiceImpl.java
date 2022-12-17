package mb.booksy.services;

import mb.booksy.domain.order.cart.Cart;
import mb.booksy.repository.ItemInCartRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    private final ItemInCartRepository itemInCartRepository;

    public CartServiceImpl(ItemInCartRepository itemInCartRepository) {
        this.itemInCartRepository = itemInCartRepository;
    }

    @Override
    public Set<Cart> findAll() {
        return null;
    }

    @Override
    public Cart findById(Long aLong) {
        return null;
    }

    @Override
    public Cart save(Cart object) {
        return null;
    }

    @Override
    public void delete(Cart object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    @Transactional
    public void deleteItemFromCart(Long cartId, Long itemId) {
        itemInCartRepository.deleteItemInCart(cartId, itemId);
    }
}
