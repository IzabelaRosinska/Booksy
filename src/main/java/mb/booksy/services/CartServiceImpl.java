package mb.booksy.services;

import mb.booksy.domain.order.cart.Cart;
import mb.booksy.repository.ItemInCartRepository;
import mb.booksy.repository.ItemRepository;
import mb.booksy.web.model.MethodDto;
import mb.booksy.web.model.PersonDto;
import mb.booksy.web.model.ReasonDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<ReasonDto> getComplaintReasons() {
        List<ReasonDto> reasons = new ArrayList<>();
        reasons.add(new ReasonDto(1L, "Uszkodzenie produktu"));
        reasons.add(new ReasonDto(2L, "Niewłaściwy produkt"));
        reasons.add(new ReasonDto(3L, "Niekompletność opakowania"));
        reasons.add(new ReasonDto(4L, "Inny powód"));
        return reasons;
    }

    @Override
    public List<MethodDto> getComplaintMethods() {
        List<MethodDto> methods = new ArrayList<>();
        methods.add(new MethodDto(1L, "Zwrot pieniędzy"));
        methods.add(new MethodDto(2L, "Wymiana poduktu"));
        methods.add(new MethodDto(3L, "Naprawa"));
        return methods;
    }
}
