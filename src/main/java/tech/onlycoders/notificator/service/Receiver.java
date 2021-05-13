package tech.onlycoders.notificator.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tech.onlycoders.notificator.dto.MessageDTO;

@RabbitListener(queues = "onlycoders_notificator")
@Component
public class Receiver {

  @RabbitHandler
  public void receive(MessageDTO in) {
    System.out.println(" [x] Received '" + in + "'");
  }
}
