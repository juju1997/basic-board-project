package basic.boardproject.repository;

import basic.boardproject.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
