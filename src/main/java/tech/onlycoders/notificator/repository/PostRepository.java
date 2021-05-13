package tech.onlycoders.notificator.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.Post;

@Repository
public interface PostRepository extends Neo4jRepository<Post, String> {}
