package mb.booksy.services;

import mb.booksy.domain.order.cart.Cart;
import mb.booksy.repository.ItemInCartRepository;
import mb.booksy.repository.ItemRepository;
import mb.booksy.web.model.PersonDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    private final ItemInCartRepository itemInCartRepository;
    private final ItemRepository itemRepository;

    public CartServiceImpl(ItemInCartRepository itemInCartRepository, ItemRepository itemRepository) {
        this.itemInCartRepository = itemInCartRepository;
        this.itemRepository = itemRepository;
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
    public void delete(Cart object) {}

    @Override
    public void deleteById(Long aLong) {}

    @Transactional
    @Override
    public void deleteItemFromCart(Long cartId, Long itemId) {
        itemInCartRepository.deleteItemInCart(cartId, itemId);
    }

    @Transactional
    @Override
    public String updateItemNumber(Long cartId, Long itemId, Integer new_number) {
        Integer maxNumber = itemRepository.findById(itemId).get().getAvailability();
        if(new_number > maxNumber)
            return "Liczba dostępnych sztuk - " + maxNumber;
        else if(new_number < 1)
            return "Liczba sztuk musi być większa od 0";
        else {
            itemInCartRepository.updateItemNumber(new_number, cartId, itemId);
            return "";
        }
    }
}
