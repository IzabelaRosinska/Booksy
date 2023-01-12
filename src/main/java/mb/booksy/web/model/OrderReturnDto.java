package mb.booksy.web.model;

import lombok.Getter;
import lombok.Setter;
import mb.booksy.domain.order.ItemInReturn;
import mb.booksy.domain.order.Order;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class OrderReturnDto {

    private Long returnId;
    private String acceptance;
    private LocalDate initDate;
    private LocalDate processDate;
    private Long OrderId;
    private boolean isValid;
}
