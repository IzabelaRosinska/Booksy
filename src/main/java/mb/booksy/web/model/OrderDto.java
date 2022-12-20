package mb.booksy.web.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderDto {

    Long id;
    Integer productNumber;
    Double amount;
    LocalDate date;
}
