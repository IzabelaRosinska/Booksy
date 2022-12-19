package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.payment.Payment;
import mb.booksy.web.model.PaymentDto;

import java.util.List;

public interface PaymentService extends BaseService<Payment, Long> {

    List<String> findPaymentImage();
    List<String> findBankImage();

    PaymentDto createBlikPayment();
    void updateOrder(PaymentDto paymentDto);

    PaymentDto createPayUPayment(PaymentDto paymentDto);
}
