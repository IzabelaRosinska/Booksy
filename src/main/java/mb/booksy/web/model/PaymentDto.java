package mb.booksy.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {

    private Long id;
    private String odbiorca;
    private String rachOdbiorca;
    private String rachNadawca;
    private Double kwota;
    private String kod;
}
