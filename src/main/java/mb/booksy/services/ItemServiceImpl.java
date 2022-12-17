package mb.booksy.services;


import mb.booksy.domain.inventory.Item;
import mb.booksy.repository.ItemRepository;
import mb.booksy.web.mapper.ItemMapper;
import mb.booksy.web.model.ItemDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;
    private ItemMapper mapper = Mappers.getMapper(ItemMapper.class);

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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
                .map(item -> getItemDetails(mapper.itemToItemDto(item)))
                .collect(Collectors.toList());

        return itemsInCart;
    }

    private ItemDto getItemDetails(ItemDto item){
        String itemPhoto = "";
        try{
            itemPhoto = new String(Base64.encodeBase64(itemRepository.findById(item.getId()).get().getItemImage()), "UTF-8");
        }catch(IOException e) {
            e.printStackTrace();
        }
        item.setImage(itemPhoto);
        item.setCartAmount(0);
        item.setCartPrice(0.0);
        return item;
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
