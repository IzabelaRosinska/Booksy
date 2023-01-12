package mb.booksy.repository;

import mb.booksy.domain.inventory.Favorite;
import mb.booksy.domain.inventory.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

//    @Query(nativeQuery = true, value = "SELECT item_id FROM favorite WHERE client_id = ?1")
//    List<Long> findClientFavorites(Long clientId);

    @Query(nativeQuery = true, value = "SELECT item_id FROM favorite WHERE client_id = ?1")
    List<Long> findClientFavorites(Long clientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM favorite WHERE (item_id = ?1 AND client_id = ?2)")
    void deleteClientFavoriteItem(Long item_id, Long clientId);

}
