package tech.onlycoders.notificator.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.DateLong;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person extends BaseEntity implements Serializable {

  private String firstName;
  private String lastName;
  private String fullName;
  private String email;
  private String imageURI;
  private String canonicalName;

  @DateLong
  private Date securityUpdate;
}
