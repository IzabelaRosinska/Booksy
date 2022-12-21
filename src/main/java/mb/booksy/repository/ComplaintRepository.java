package mb.booksy.repository;

import mb.booksy.domain.order.Complaint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ComplaintRepository extends CrudRepository<Complaint, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM complaints INNER JOIN orders ON complaints.order_id = orders.id INNER JOIN clients ON orders.client_id = clients.id WHERE clients.id = ?1")
    List<Complaint> findAllByUserId(Long clientId);
}
