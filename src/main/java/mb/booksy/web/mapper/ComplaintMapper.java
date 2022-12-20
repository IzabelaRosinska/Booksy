package mb.booksy.web.mapper;

import mb.booksy.domain.order.Complaint;
import mb.booksy.web.model.ComplaintDto;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class ComplaintMapper {

    public Complaint complaintDtoToComplaint(ComplaintDto complaintDto) {
        Complaint complaint = new Complaint();
        complaint.setReasonComplaint(complaintDto.getCompReason());
        complaint.setMethodComplaint(complaintDto.getCompMethod());
        complaint.setItemId(complaintDto.getCompItem());
        complaint.setInitDate(LocalDate.now());
        complaint.setAcceptance(false);
        complaint.setExtraInformation(complaintDto.getExtraInformation());

        return complaint;
    }

    public ComplaintDto complaintToComplaintDto(Complaint complaint) {
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setCompId(complaint.getId());
        complaintDto.setOrderId(complaint.getOrder().getId());
        complaintDto.setInitDate(complaint.getInitDate());
        complaintDto.setCompMethod(complaint.getMethodComplaint());
        complaintDto.setCompReason(complaint.getReasonComplaint());
        return complaintDto;
    }
}
