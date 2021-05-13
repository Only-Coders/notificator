package tech.onlycoders.notificator.repository;

import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.GitPlatform;

@Repository
public interface GitPlatformRepository extends Neo4jRepository<GitPlatform, String> {
  Optional<GitPlatform> findById(String id);
}
