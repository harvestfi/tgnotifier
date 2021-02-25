package pro.belbix.tgnotifier.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.belbix.tgnotifier.db.entity.TokenWatchEntity;

public interface TokenWatchRepository extends JpaRepository<TokenWatchEntity, Long> {

}
