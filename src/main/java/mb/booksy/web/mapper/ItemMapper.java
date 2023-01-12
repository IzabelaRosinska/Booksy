package mb.booksy.web.mapper;


import mb.booksy.domain.inventory.Item;
import mb.booksy.web.model.ItemDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ItemMapper {

    public ItemDto itemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setItemName(item.getItemName());
        itemDto.setProducerName(item.getProducerName());
        itemDto.setPrice(item.getPrice());
        itemDto.setGenre(item.getGenre());
        itemDto.setBookType(item.getBookType());
        itemDto.setItemDescription(item.getItemDescription());
        itemDto.setAvailability(item.getAvailability());
        return itemDto;
    }

    public ItemDto itemToItemDtoFull(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setItemName(item.getItemName());
        itemDto.setProducerName(item.getProducerName());
        itemDto.setPrice(item.getPrice());
        itemDto.setGenre(item.getGenre());
        itemDto.setBookType(item.getBookType());
        itemDto.setItemDescription(item.getItemDescription());
        itemDto.setAvailability(item.getAvailability());

        String itemPhoto = "";
        try{
            itemPhoto = new String(Base64.encodeBase64(item.getItemImage()), "UTF-8");
        }catch(IOException e) {
            e.printStackTrace();
        }

        itemDto.setImage(itemPhoto);

        return itemDto;
    }
}

