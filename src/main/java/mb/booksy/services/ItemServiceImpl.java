package mb.booksy.services;


import mb.booksy.domain.inventory.Item;
import mb.booksy.repository.ItemInCartRepository;
import mb.booksy.repository.ItemRepository;
import mb.booksy.web.mapper.ItemMapper;
import mb.booksy.web.model.ItemDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mapstruct.factory.Mappers;
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
    private ItemMapper mapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemInCartRepository itemInCartRepository, ItemMapper mapper) {
        this.itemRepository = itemRepository;
        this.itemInCartRepository = itemInCartRepository;
        this.mapper = mapper;
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
                .map(item -> getItemDetails(mapper.itemToItemDto(item), cartId))
                .collect(Collectors.toList());

        return itemsInCart;
    }

    private ItemDto getItemDetails(ItemDto item, Long cartId){
        String itemPhoto = "";
        try{
            itemPhoto = new String(Base64.encodeBase64(itemRepository.findById(item.getId()).get().getItemImage()), "UTF-8");
        }catch(IOException e) {
            e.printStackTrace();
        }
        item.setImage(itemPhoto);
        int amount = itemInCartRepository.findItemInCart(item.getId(), cartId).get(0).getNumber();
        double price = amount * itemRepository.findById(item.getId()).get().getPrice();
        item.setCartAmount(amount);
        item.setCartPrice(price);
        return item;
    }

    @Override
    public Double countPrice(Long cartId) {
        double price = 0;
        List<ItemDto> items = findAllCartItem(cartId);
        for(ItemDto item: items) {
            price += item.getCartPrice();
        }
        return price;
    }

    @Override
    public Double countDiscount(Long cartId) {
        double newPrice = countPrice(cartId) * 90;
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

    public String createImage(String path) {
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
