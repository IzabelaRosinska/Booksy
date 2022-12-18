package mb.booksy.services;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.payment.Payment;

import java.util.List;

public interface PaymentService extends BaseService<Payment, Long> {

    List<String> findPaymentImage();
    List<String> findBankImage();
}
