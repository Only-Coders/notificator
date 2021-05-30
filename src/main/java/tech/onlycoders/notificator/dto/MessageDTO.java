package tech.onlycoders.notificator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
}
