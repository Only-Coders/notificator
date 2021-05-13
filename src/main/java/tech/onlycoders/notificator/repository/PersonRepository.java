package tech.onlycoders.notificator.repository;

import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import tech.onlycoders.notificator.model.Person;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, String> {
  Optional<Person> findByEmail(String email);
}
