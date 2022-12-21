package mb.booksy.repository;

import mb.booksy.domain.order.payment.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

}
