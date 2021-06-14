package tech.onlycoders.notificator;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableRabbit
@EnableScheduling
public class MessagingRabbitmqApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(MessagingRabbitmqApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {
    Thread.currentThread().join();
  }
}
