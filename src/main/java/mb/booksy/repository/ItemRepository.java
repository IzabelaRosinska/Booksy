package mb.booksy.repository;

import mb.booksy.domain.inventory.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM items INNER JOIN itemsInCarts ON itemsInCarts.item_id = items.id WHERE (itemsInCarts.cart_id == ?1)")
    List<Item> findAllInCart(Long cartId);


}
