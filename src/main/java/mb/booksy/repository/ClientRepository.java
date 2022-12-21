package mb.booksy.repository;

import mb.booksy.domain.user.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Query(nativeQuery = true, value = "select * from clients where email = ?1")
    Client selectClientByUsername(String username);
}
