package tech.onlycoders.notificator.repository;

import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.FCMToken;

@Repository
public interface FCMTokenRepository extends Neo4jRepository<FCMToken, String> {
  @Query("MATCH (t:FCMToken{id: $id}) DETACH DELETE t;")
  void deleteById(String id);

  @Query("MATCH (t:FCMToken)<-[:OWNS]-(:User{canonicalName: $canonicalName}) RETURN t;")
  List<FCMToken> getUserTokens(String canonicalName);
}
