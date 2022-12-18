package mb.booksy;

import lombok.extern.slf4j.Slf4j;
import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.cart.Cart;
import mb.booksy.domain.order.cart.ItemInCart;
import mb.booksy.domain.order.delivery.DeliveryPoint;
import mb.booksy.domain.order.delivery.InpostBox;
import mb.booksy.repository.*;
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
    private final InpostBoxRepository inpostBoxRepository;
    private final DeliveryPointRepository deliveryPointRepository;

    public DataLoader(CartRepository cartRepository, ItemRepository itemRepository, ItemInCartRepository itemInCartRepository, InpostBoxRepository inpostBoxRepository, DeliveryPointRepository deliveryPointRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.itemInCartRepository = itemInCartRepository;
        this.inpostBoxRepository = inpostBoxRepository;
        this.deliveryPointRepository = deliveryPointRepository;
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

        inpostBoxRepository.save(InpostBox.builder().id(1L).boxAddress("ul. Boguszowska 82, 54-046 Wrocław").build());
        inpostBoxRepository.save(InpostBox.builder().id(2L).boxAddress("ul. Biała 19, 55-041 Wrocław").build());
        inpostBoxRepository.save(InpostBox.builder().id(3L).boxAddress("ul. Czekoladowa 12, 32-111 Warszawa").build());
        inpostBoxRepository.save(InpostBox.builder().id(4L).boxAddress("ul. Niedźwiedzia 1, 76-523 Warszawa").build());
        inpostBoxRepository.save(InpostBox.builder().id(5L).boxAddress("ul. Żółta 10, 32-234 Gdańsk").build());
        inpostBoxRepository.save(InpostBox.builder().id(6L).boxAddress("ul. Kamieńskiego 112b, 23-987 Szczecin").build());
        inpostBoxRepository.save(InpostBox.builder().id(7L).boxAddress("ul. Żeromskiego 3, 55-210 Szczecin").build());
        inpostBoxRepository.save(InpostBox.builder().id(8L).boxAddress("ul. Kazimierza Wielkiego, 34-112 Kraków").build());
        inpostBoxRepository.save(InpostBox.builder().id(9L).boxAddress("ul. Ziemna 1, 76-433 Legnica").build());
        inpostBoxRepository.save(InpostBox.builder().id(10L).boxAddress("ul. Karmelkowa 76, 54-044 Poznań").build());

        deliveryPointRepository.save(DeliveryPoint.builder().id(1L).pointName("Żabka").addressName("ul. Boguszowska 82, 54-046 Wrocław").build());
        deliveryPointRepository.save(DeliveryPoint.builder().id(2L).pointName("Żabka").addressName("ul. Biała 19, 55-041 Wrocław").build());
        deliveryPointRepository.save(DeliveryPoint.builder().id(3L).pointName("Żabka").addressName("ul. Czekoladowa 12, 32-111 Warszawa").build());
        deliveryPointRepository.save(DeliveryPoint.builder().id(4L).pointName("Żabka").addressName("ul. Niedźwiedzia 1, 76-523 Warszawa").build());
        deliveryPointRepository.save(DeliveryPoint.builder().id(5L).pointName("Żabka").addressName("ul. Żółta 10, 32-234 Gdańsk").build());
        deliveryPointRepository.save(DeliveryPoint.builder().id(6L).pointName("Żabka").addressName("ul. Kamieńskiego 112b, 23-987 Szczecin").build());
        deliveryPointRepository.save(DeliveryPoint.builder().id(7L).pointName("Kiosk RUCH").addressName("ul. Żeromskiego 3, 55-210 Szczecin").build());
        deliveryPointRepository.save(DeliveryPoint.builder().id(8L).pointName("Kiosk RUCH").addressName("ul. Kazimierza Wielkiego, 34-112 Kraków").build());
        deliveryPointRepository.save(DeliveryPoint.builder().id(9L).pointName("Kiosk RUCH").addressName("ul. Ziemna 1, 76-433 Legnica").build());
        deliveryPointRepository.save(DeliveryPoint.builder().id(10L).pointName("Kiosk RUCH").addressName("ul. Karmelkowa 76, 54-044 Poznań").build());
    }



}
