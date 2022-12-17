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
        Item item1 = new Item(1L, "Hobbit, czyli tam i spowrotem", "J.R.R. Tolkien", 21.46, 12, createImage("src/main/resources/static/resources/images/hobbit.jpg"));
        Item item2 = new Item(2L, "Harry Potter. Tom 1", "Rowling J. K.", 20.99, 3, createImage("src/main/resources/static/resources/images/harry.jpg"));
        Item item3 = new Item(3L,"Igrzyska śmierci. Tom 3", "Collins Suzanne", 17.21, 1, createImage("src/main/resources/static/resources/images/igrzyska.jpg"));
        Item item4 = new Item(4L, "Kubuś Puchatek", "Milne Alan Alexander", 9.99, 23, createImage("src/main/resources/static/resources/images/puchatek.jpg"));
        Item item5 = new Item(5L, "Władca Pierścieni. Bractwo pierścienia.", "Tolkien John Ronald Reuel", 47.81, 7, createImage("src/main/resources/static/resources/images/wladca.jpg"));
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        itemRepository.save(item5);

        Cart cart1 = new Cart(1L);
        Cart cart2 = new Cart(2L);
        Cart cart3 = new Cart(3L);
        Cart cart4 = new Cart(4L);
        cartRepository.save(cart1);
        cartRepository.save(cart2);
        cartRepository.save(cart3);
        cartRepository.save(cart4);

        itemInCartRepository.save(new ItemInCart(1L, 2, item1, cart1));
        itemInCartRepository.save(new ItemInCart(2L, 1, item2, cart1));
        itemInCartRepository.save(new ItemInCart(3L, 3, item4, cart1));
        itemInCartRepository.save(new ItemInCart(4L, 1, item5, cart1));

        itemInCartRepository.save(new ItemInCart(5L, 1, item2, cart2));

        itemInCartRepository.save(new ItemInCart(6L, 1, item1, cart3));
        itemInCartRepository.save(new ItemInCart(7L, 1, item3, cart3));
        itemInCartRepository.save(new ItemInCart(8L, 2, item4, cart3));

        itemInCartRepository.save(new ItemInCart(9L, 2, item1, cart4));
        itemInCartRepository.save(new ItemInCart(10L, 3, item5, cart4));
    }

}
