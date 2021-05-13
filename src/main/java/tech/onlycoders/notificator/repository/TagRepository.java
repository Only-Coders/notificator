package tech.onlycoders.notificator.repository;

import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.Tag;

@Repository
public interface TagRepository extends Neo4jRepository<Tag, String> {
  Optional<Tag> findByCanonicalName(String canonicalName);
}
