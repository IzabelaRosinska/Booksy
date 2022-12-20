package mb.booksy.repository;

import mb.booksy.domain.order.delivery.Delivery;
import mb.booksy.domain.order.payment.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM payments ORDER BY id DESC")
    List<Delivery> findLastIndex();
}
