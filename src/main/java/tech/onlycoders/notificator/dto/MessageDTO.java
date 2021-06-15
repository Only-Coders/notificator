package tech.onlycoders.notificator.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO implements Serializable {

  private EventType eventType;
  private String message;
  private String to;

  @Builder.Default
  private Boolean read = false;

  private String from;
  private Long createdAt = new Date().toInstant().toEpochMilli();
  private String imageURI;
}
