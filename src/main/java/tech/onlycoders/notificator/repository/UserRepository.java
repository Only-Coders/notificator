package tech.onlycoders.notificator.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
  @Query(
    " MATCH (n:NotificationConfig{type:$type})<-[r:CONFIGURES]-(u:User{email:$email})" +
    " RETURN u, COLLECT(r), COLLECT(n)"
  )
  Optional<User> getUserNotificationConfig(String type, String email);

  @Query(
    " MATCH (me:User{email: $email}) " +
    " WITH me " +
    " CALL { " +
    "   WITH me" +
    "   MATCH (n:NotificationConfig{type: $type})<-[r:CONFIGURES]-(u:User)-[:IS_CONNECTED]-(me) " +
    "   RETURN u, r, n " +
    " UNION " +
    "   WITH me" +
    "   MATCH (me)<-[:FOLLOWS]-(u:User)-[r:CONFIGURES]->(n:NotificationConfig{type: $type}) " +
    "   RETURN u, r, n " +
    " } " +
    " RETURN u, collect(n), collect(r) "
  )
  List<User> getUserContactsAndFollowers(String email, String type);
}
