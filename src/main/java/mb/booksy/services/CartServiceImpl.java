package mb.booksy.services;

import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.user.Client;
import mb.booksy.repository.CartRepository;
import mb.booksy.repository.ClientRepository;
import mb.booksy.repository.ItemInCartRepository;
import mb.booksy.repository.ItemRepository;
import mb.booksy.web.model.MethodDto;
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
    private final CartRepository cartRepository;
    private final ItemService itemService;
    private final UserAuthenticationService userAuthenticationService;
    private final ClientRepository clientRepository;

    public CartServiceImpl(ItemInCartRepository itemInCartRepository, ItemRepository itemRepository, CartRepository cartRepository, ItemService itemService, UserAuthenticationService userAuthenticationService, ClientRepository clientRepository) {
        this.itemInCartRepository = itemInCartRepository;
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.itemService = itemService;
        this.userAuthenticationService = userAuthenticationService;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void deleteItemFromCart(Long itemId) {
        itemInCartRepository.deleteItemInCart(itemService.getCurrentCartId(), itemId);
    }

    @Override
    @Transactional
    public String updateItemNumber(Long itemId, Integer newNumber) {
        Integer maxNumber = itemRepository.findById(itemId).get().getAvailability();
        if(newNumber > maxNumber)
            return "Liczba dostępnych sztuk - " + maxNumber;
        else if(newNumber < 1)
            return "Liczba sztuk musi być większa od 0";
        else {
            itemInCartRepository.updateItemNumber(newNumber, itemService.getCurrentCartId(), itemId);
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

    @Override
    public Integer getPoints() {
        return ((Client)userAuthenticationService.getAuthenticatedUser()).getLoyaltyPoints();
    }

    @Override
    @Transactional
    public void addPoints() {
        Long cartId = itemService.getCurrentCartId();
        Cart cart = cartRepository.findByCartId(cartId);
        Client client = (Client)userAuthenticationService.getAuthenticatedUser();
        Integer points = client.getLoyaltyPoints();
        cart.setPoints(points);
        client.setLoyaltyPoints(0);
        cartRepository.save(cart);
        clientRepository.save(client);
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
}
