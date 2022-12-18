package mb.booksy.web.mapper;

import mb.booksy.domain.order.delivery.InpostBox;
import mb.booksy.web.model.InpostBoxDto;
import org.springframework.stereotype.Component;

@Component
public class InpostBoxMapper {

    public InpostBoxDto boxToBoxDto(InpostBox inpostBox) {
        InpostBoxDto boxDto = new InpostBoxDto();
        boxDto.setId(inpostBox.getId());
        boxDto.setBoxAddress(inpostBox.getBoxAddress());
        return boxDto;
    }
}
