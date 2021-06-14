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

  @Query("MATCH (u:User) WHERE u.eliminationDate < $now RETURN u.email")
  List<String> getUsersToDelete(long now);

  @Query(
          "MATCH (target:User{email: $email})\n" +
                  "    OPTIONAL MATCH (target)-[:PUBLISH]->(post:Post)\n" +
                  "    OPTIONAL MATCH (target)-[:WORKS]->(workPosition:WorkPosition)\n" +
                  "    OPTIONAL MATCH (target)-[:STUDIES]->(degree:Degree)\n" +
                  "    OPTIONAL MATCH (target)-[:CREATES]->(report:Report) \n" +
                  "    OPTIONAL MATCH (target)-[:SENDS|:TO]-(contactRequest:ContactRequest)\n" +
                  "    OPTIONAL MATCH (post)<-[:TO]-(postReaction:Reaction)\n" +
                  "    OPTIONAL MATCH (post)<-[:FOR]-(postComment:Comment) \n" +
                  "    OPTIONAL MATCH (post)<-[:HAS]-(postReport:Report) \n" +
                  "    OPTIONAL MATCH (postReaction)<-[:TO]-(commentReaction:Reaction) \n" +
                  "detach delete commentReaction, postReport, postComment, postReaction,\n" +
                  "contactRequest, report, degree, workPosition, post, target;"
  )
  void deleteUser(String email);
}
