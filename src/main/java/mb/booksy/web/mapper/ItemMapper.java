package mb.booksy.web.mapper;


import mb.booksy.domain.inventory.Item;
import mb.booksy.web.model.ItemDto;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

//@Mapper
@Component
public class ItemMapper {

    public ItemDto itemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setItemName(item.getItemName());
        return itemDto;
    }
    //public Item itemDtoToItem(ItemDto dto);
/*
    @Mappings({
            @Mapping(target="id", source="dto.id"),
            @Mapping(target="upc", source="dto.upc"),
            @Mapping(target="itemName", source="dto.itemName"),
            @Mapping(target="itemBrand", source="dto.itemBrand"),
            @Mapping(target="itemModel", source="dto.itemModel"),
            @Mapping(target="itemDescription", source="dto.itemDescription"),
            @Mapping(target="itemImage", source="dto.itemImage"),
            @Mapping(target="dayRentPrice", source="dto.dayRentPrice"),
            @Mapping(target="category", source="dto.category"),
            @Mapping(target="userId", source="dto.userId")
    })
    Item itemDtoToItem(ItemDto dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(ItemDto dto, @MappingTarget Item entity);


 */
}

