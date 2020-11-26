package pro.belbix.tgnotifier.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.belbix.tgnotifier.db.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
