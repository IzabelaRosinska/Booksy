package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.repository.ItemInCartRepository;
import mb.booksy.repository.ItemRepository;
import mb.booksy.web.mapper.ItemMapper;
import mb.booksy.web.model.ItemDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemInCartRepository itemInCartRepository;
    private ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemInCartRepository itemInCartRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemInCartRepository = itemInCartRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public Set<Item> findAll() {
        Set<Item> items = new HashSet<>();
        itemRepository.findAll().forEach(items::add);
        return items;
    }

    @Override
    public List<ItemDto> findAllCartItem(Long cartId) {
        List<ItemDto> itemsInCart = itemRepository.findAllInCart(cartId)
                .stream()
                .map(item -> getItemDetails(itemMapper.itemToItemDto(item), cartId))
                .collect(Collectors.toList());

        return itemsInCart;
    }

    private ItemDto getItemDetails(ItemDto itemDto, Long cartId){
        String itemPhoto = "";
        try{
            itemPhoto = new String(Base64.encodeBase64(itemRepository.findById(itemDto.getId()).get().getItemImage()), "UTF-8");
        }catch(IOException e) {
            e.printStackTrace();
        }
        itemDto.setImage(itemPhoto);
        int amount = itemInCartRepository.findItemInCart(itemDto.getId(), cartId).get(0).getNumber();
        double price = amount * itemRepository.findById(itemDto.getId()).get().getPrice();
        itemDto.setCartAmount(amount);
        itemDto.setCartPrice(price);
        return itemDto;
    }

    @Override
    public Double countPrice(Long cartId) {
        return itemInCartRepository.countCartPrice(cartId);
    }

    @Override
    public Double countDiscount(Long cartId) {
        double newPrice = itemInCartRepository.countCartDiscount(cartId);
        newPrice = Math.round(newPrice);
        newPrice /= 100;
        return newPrice;
    }

    @Override
    public List<String> findDeliveryImage() {
        List<String> images = new ArrayList<>();
        images.add(createImage("src/main/resources/static/resources/images/box.jpg"));
        images.add(createImage("src/main/resources/static/resources/images/dhl.jpg"));
        images.add(createImage("src/main/resources/static/resources/images/zabka.jpg"));
        images.add(createImage("src/main/resources/static/resources/images/ruch.jpg"));
        return images;
    }

    private String createImage(String path) {
        byte[] array = null;
        String itemPhoto = "";
        try {
            array = Files.readAllBytes(Paths.get(path));
            itemPhoto = new String(Base64.encodeBase64(array), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemPhoto;
    }

    @Override
    public List<ItemDto> findAllOrderItem(String orderId, Long cartId) {
        List<ItemDto> itemsInOrder = itemRepository.findAllInOrder(orderId)
                .stream()
                .map(item -> getItemDetails(itemMapper.itemToItemDto(item), cartId))
                .collect(Collectors.toList());

        return itemsInOrder;
    }

    @Override
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    @Override
    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void delete(Item item) {
        itemRepository.delete(item);
    }

    @Override
    public void deleteById(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
