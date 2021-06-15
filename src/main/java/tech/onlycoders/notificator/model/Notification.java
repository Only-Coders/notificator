package tech.onlycoders.notificator.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.onlycoders.notificator.dto.EventType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification implements Serializable {

  private EventType eventType;
  private String message;
  private String imageURI;
  private String from;
  private String canonicalName;

  @Builder.Default
  private Boolean read = false;

  private Long createdAt;
}
