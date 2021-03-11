package pro.belbix.tgnotifier.db.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.belbix.tgnotifier.db.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  @EntityGraph(value = "users-graph.all", type = EntityGraphType.LOAD)
  List<UserEntity> findAll();

}
