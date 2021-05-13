package tech.onlycoders.notificator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.Organization;

@Repository
public interface OrganizationRepository extends Neo4jRepository<Organization, String> {
  Page<Organization> findByNameContainingIgnoreCase(String canonicalName, Pageable pageable);
}
