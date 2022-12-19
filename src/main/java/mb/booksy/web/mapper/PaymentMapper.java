package mb.booksy.web.mapper;

import mb.booksy.domain.order.payment.Payment;
import mb.booksy.web.model.PaymentDto;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDto paymentToPaymentDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(payment.getId());
        paymentDto.setKwota(payment.getAmount());
        return paymentDto;
    }
}
