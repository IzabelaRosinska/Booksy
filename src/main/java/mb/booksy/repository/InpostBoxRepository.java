package mb.booksy.repository;

import mb.booksy.domain.inventory.Item;
import mb.booksy.domain.order.delivery.InpostBox;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InpostBoxRepository extends CrudRepository<InpostBox, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM inpost_boxes")
    List<InpostBox> findAll();
}
