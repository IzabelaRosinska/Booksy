package mb.booksy.web.mapper;


import mb.booksy.domain.inventory.Item;
import mb.booksy.web.model.ItemDto;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemDto itemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setItemName(item.getItemName());
        return itemDto;
    }
}

