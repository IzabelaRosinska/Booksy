package mb.booksy;

import lombok.extern.slf4j.Slf4j;
import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.order.cart.ItemInCart;
import mb.booksy.repository.CartRepository;
import mb.booksy.repository.ItemInCartRepository;
import mb.booksy.repository.ItemRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final ItemInCartRepository itemInCartRepository;

    public DataLoader(CartRepository cartRepository, ItemRepository itemRepository, ItemInCartRepository itemInCartRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.itemInCartRepository = itemInCartRepository;
    }

    public byte[] createImage(String path) {
        byte[] array = null;
        try {
            array = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Item item1 = Item.builder().id(1L).itemName("Hobbit, czuli tam i spowrotem").producerName("J.R.R. Tolkien").price(21.45).availability(12).itemImage(createImage("src/main/resources/static/resources/images/doctor.jpg")).build();
        Item item2 = Item.builder().id(2L).itemName("Harry Potter. Tom 1").producerName("Rowling J. K.").price(20.99).availability(3).itemImage(createImage("src/main/resources/static/resources/images/harry.jpg")).build();
        Item item3 = Item.builder().id(3L).itemName("Igrzyska śmierci. Tom 3").producerName("Collins Suzanne").price(17.21).availability(1).itemImage(createImage("src/main/resources/static/resources/images/igrzyska.jpg")).build();
        Item item4 = Item.builder().id(4L).itemName("Kubuś Puchatek").producerName("Milne Alan Alexander").price(9.99).availability(23).itemImage(createImage("src/main/resources/static/resources/images/puchatek.jpg")).build();
        Item item5 = Item.builder().id(5L).itemName("Władca Pierścieni. Bractwo pierścienia.").producerName("Tolkien John Ronald Reuel").price(47.81).availability(7).itemImage(createImage("src/main/resources/static/resources/images/wladca.jpg")).build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        itemRepository.save(item5);

        Cart cart1 = Cart.builder().id(1L).build();
        Cart cart2 = Cart.builder().id(2L).build();
        Cart cart3 = Cart.builder().id(3L).build();
        Cart cart4 = Cart.builder().id(4L).build();
        cartRepository.save(cart1);
        cartRepository.save(cart2);
        cartRepository.save(cart3);
        cartRepository.save(cart4);

        itemInCartRepository.save(ItemInCart.builder().id(1L).item(item1).cart(cart1).number(2).build());
        itemInCartRepository.save(ItemInCart.builder().id(2L).item(item2).cart(cart1).number(1).build());
        itemInCartRepository.save(ItemInCart.builder().id(3L).item(item4).cart(cart1).number(3).build());
        itemInCartRepository.save(ItemInCart.builder().id(4L).item(item5).cart(cart1).number(1).build());

        itemInCartRepository.save(ItemInCart.builder().id(5L).item(item2).cart(cart2).number(1).build());

        itemInCartRepository.save(ItemInCart.builder().id(6L).item(item1).cart(cart3).number(1).build());
        itemInCartRepository.save(ItemInCart.builder().id(7L).item(item3).cart(cart3).number(1).build());
        itemInCartRepository.save(ItemInCart.builder().id(8L).item(item4).cart(cart3).number(2).build());

        itemInCartRepository.save(ItemInCart.builder().id(9L).item(item1).cart(cart4).number(2).build());
        itemInCartRepository.save(ItemInCart.builder().id(10L).item(item5).cart(cart4).number(3).build());
    }

}
