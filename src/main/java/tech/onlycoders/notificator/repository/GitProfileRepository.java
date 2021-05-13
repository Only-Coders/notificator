package tech.onlycoders.notificator.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.GitProfile;

@Repository
public interface GitProfileRepository extends Neo4jRepository<GitProfile, String> {}
