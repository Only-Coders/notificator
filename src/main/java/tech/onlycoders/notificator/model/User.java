package tech.onlycoders.notificator.model;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Node;

@EqualsAndHashCode(callSuper = true)
@Node({ "User", "Person" })
@Data
@NoArgsConstructor
public class User extends Person implements Serializable {

  private Boolean defaultPrivacyIsPublic = false;

  private Boolean blocked = false;
}
