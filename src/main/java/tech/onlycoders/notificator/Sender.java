package tech.onlycoders.notificator;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.onlycoders.notificator.dto.MessageDTO;

//@Service
public class Sender {

  private final RabbitTemplate template;
  private final Queue queue;

  public Sender(RabbitTemplate template, Queue queue) {
    this.template = template;
    this.queue = queue;
  }

  public void send() {
    MessageDTO message = new MessageDTO();
    this.template.convertAndSend(queue.getName(), message);
    System.out.println(" [x] Sent '" + message + "'");
  }
}
