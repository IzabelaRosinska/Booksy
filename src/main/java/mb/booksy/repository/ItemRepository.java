package mb.booksy.repository;

import mb.booksy.domain.inventory.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM items INNER JOIN items_in_carts ON items_in_carts.item_id = items.id WHERE items_in_carts.cart_id = ?1")
    List<Item> findAllInCart(Long cartId);


}
