package tech.onlycoders.notificator.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
  @Query("MATCH (u:User{email :$email}) return u")
  Optional<User> findByEmail(String email);
}
