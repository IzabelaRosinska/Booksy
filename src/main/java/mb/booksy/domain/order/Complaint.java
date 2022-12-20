package mb.booksy.domain.order;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mb.booksy.domain.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "complaints")
public class Complaint extends BaseEntity {

    @Builder
    public Complaint(Long id, String reasonComplaint, LocalDate initDate, LocalDate processDate) {
        super(id);
        this.reasonComplaint = reasonComplaint;
        this.initDate = initDate;
        this.processDate = processDate;
    }

    @NotBlank
    @NotEmpty
    @Column(name = "reason_complaint")
    private String reasonComplaint;

    @NotBlank
    @NotEmpty
    @Column(name = "method_complaint")
    private String methodComplaint;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "extra")
    private String extraInformation;

    @Column(name = "acceptance")
    private Boolean acceptance;

    @Column(name = "init_date")
    private LocalDate initDate;

    @Column(name = "process_date")
    private LocalDate processDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
