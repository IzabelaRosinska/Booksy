package mb.booksy.web.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ComplaintDto {

    private Long compId;
    private Long orderId;
    private LocalDate initDate;
    private Long compItem;
    private String compItemName;
    private String compReason;
    private String compMethod;
    private String extraInformation;
    private String acceptance;
}
