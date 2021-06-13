package tech.onlycoders.notificator.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@EqualsAndHashCode(callSuper = true)
@Node({ "User", "Person" })
@Data
@NoArgsConstructor
public class User extends Person implements Serializable {

  private Boolean defaultPrivacyIsPublic = false;

  private Boolean blocked = false;

  @Relationship(type = "CONFIGURES", direction = Relationship.Direction.OUTGOING)
  public Set<NotificationConfig> configs = new HashSet<>();
}
