package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);


}

package fourpetals.com.repository;

public class UserRepository {

}
