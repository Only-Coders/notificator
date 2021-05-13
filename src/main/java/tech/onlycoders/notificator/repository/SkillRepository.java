package tech.onlycoders.notificator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.Skill;

@Repository
public interface SkillRepository extends Neo4jRepository<Skill, String> {
  Page<Skill> findByNameContainingIgnoreCase(String skillName, PageRequest pageRequest);
}
