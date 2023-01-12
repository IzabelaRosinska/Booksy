package mb.booksy.web.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemInReturnDto {

    private Long id;
    private Integer number;
    private Long itemId;
    private Long orderReturnId;
}
