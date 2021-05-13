package tech.onlycoders.notificator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.EducationalOrganization;

@Repository
public interface EducationalOrganizationRepository extends Neo4jRepository<EducationalOrganization, String> {
  Page<EducationalOrganization> findByNameContainingIgnoreCase(String canonicalName, Pageable pageable);
}
