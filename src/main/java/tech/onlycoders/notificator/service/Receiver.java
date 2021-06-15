package tech.onlycoders.notificator.service;

import java.util.concurrent.ExecutionException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tech.onlycoders.notificator.dto.EventType;
import tech.onlycoders.notificator.dto.MessageDTO;
import tech.onlycoders.notificator.model.Notification;
import tech.onlycoders.notificator.model.NotificationConfig;
import tech.onlycoders.notificator.model.User;
import tech.onlycoders.notificator.repository.FCMTokenRepository;
import tech.onlycoders.notificator.repository.UserRepository;

@RabbitListener(queues = "onlycoders_notificator")
@Component
public class Receiver {

  private final FirebaseService firebaseService;
  private final UserRepository userRepository;
  private final MailService mailService;
  private final FCMTokenRepository fcmTokenRepository;

  public Receiver(
    FirebaseService firebaseService,
    UserRepository userRepository,
    MailService mailService,
    FCMTokenRepository fcmTokenRepository
  ) {
    this.firebaseService = firebaseService;
    this.userRepository = userRepository;
    this.mailService = mailService;
    this.fcmTokenRepository = fcmTokenRepository;
  }

  @RabbitHandler
  public void receive(MessageDTO message) {
    System.out.println(" [x] Received '" + message + "'");
    this.userRepository.getUserNotificationConfig(message.getEventType().name(), message.getTo())
      .ifPresentOrElse(
        user ->
          user
            .getConfigs()
            .stream()
            .filter(notiConfig -> notiConfig.getType().name().equalsIgnoreCase(message.getEventType().name()))
            .findFirst()
            .ifPresentOrElse(
              notificationConfig -> notifyUser(message, user, notificationConfig),
              () -> System.out.println("[x] Notification Config not found for '" + message.getTo() + "'")
            ),
        () -> System.out.println("[x] User not found '" + message.getTo() + "'")
      );
  }

  private void notifyUser(MessageDTO message, User sourceUser, NotificationConfig notificationConfig) {
    var notification = Notification
      .builder()
      .message(message.getMessage())
      .from(message.getFrom())
      .canonicalName(sourceUser.getCanonicalName())
      .eventType(message.getEventType())
      .read(false)
      .imageURI(message.getImageURI())
      .createdAt(message.getCreatedAt())
      .build();
    if (message.getEventType() == EventType.NEW_POST) {
      this.userRepository.getUserContactsAndFollowers(sourceUser.getEmail(), message.getEventType().name())
        .forEach(
          contact ->
            contact
              .getConfigs()
              .stream()
              .filter(notiConfig -> notiConfig.getType().name().equalsIgnoreCase(message.getEventType().name()))
              .findFirst()
              .ifPresentOrElse(
                userConfig -> {
                  sendNotification(notification, contact, userConfig);
                },
                () -> System.out.println("[x] Notification Config not found for '" + contact.getEmail() + "'")
              )
        );
    } else {
      sendNotification(notification, sourceUser, notificationConfig);
    }
  }

  private void sendNotification(Notification notification, User user, NotificationConfig notificationConfig) {
    try {
      if (notificationConfig.getEmail()) {
        System.out.println("[MAIL] " + notification.getEventType() + " to: " + user.getEmail());
        this.mailService.sendMail("OnlyCoders Notifications", user.getEmail(), notification.getMessage());
      }
      if (notificationConfig.getPush()) {
        this.firebaseService.storeNotification(notification, user.getCanonicalName());

        var tokens = this.fcmTokenRepository.getUserTokens(user.getCanonicalName());
        tokens.forEach(fcmToken -> this.firebaseService.sendPushNotification(notification, fcmToken));
        System.out.println("[PUSH] " + notification.getEventType() + " to: " + user.getEmail());
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
      System.out.println("[x] Error sending notification to '" + user.getCanonicalName() + "'");
    }
  }
}
