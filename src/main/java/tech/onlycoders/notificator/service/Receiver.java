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
    this.userRepository.getUserNotificationConfig(message.getEventType().name(), message.getFrom())
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
      .eventType(message.getEventType())
      .message(message.getMessage())
      .from(sourceUser.getFullName())
      .canonicalName(sourceUser.getCanonicalName())
      .imageURI(sourceUser.getImageURI())
      .createdAt(message.getCreatedAt())
      .read(false)
      .build();

    addFollowerContactStatus(message, notification);

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
      this.userRepository.findByEmail(message.getTo())
        .ifPresent(targetUser -> sendNotification(notification, targetUser, notificationConfig));
    }
  }

  private void addFollowerContactStatus(MessageDTO message, Notification notification) {
    if (message.getEventType() == EventType.CONTACT_REQUEST || message.getEventType() == EventType.NEW_FOLLOWER) {
      notification.setSourceIsFollower(userRepository.isFollower(message.getFrom(), message.getTo()));
      notification.setSourceIsContact(userRepository.isContact(message.getFrom(), message.getTo()));
    }
  }

  private void sendNotification(Notification notification, User targetUser, NotificationConfig notificationConfig) {
    try {
      if (notificationConfig.getEmail()) {
        System.out.println("[MAIL] " + notification.getEventType() + " to: " + targetUser.getEmail());
        this.mailService.sendMail("OnlyCoders Notifications", targetUser.getEmail(), notification.getMessage());
      }
      if (notificationConfig.getPush()) {
        this.firebaseService.storeNotification(notification, targetUser.getCanonicalName());

        var tokens = this.fcmTokenRepository.getUserTokens(targetUser.getEmail());
        tokens.forEach(fcmToken -> this.firebaseService.sendPushNotification(notification, fcmToken));
        System.out.println("[PUSH] " + notification.getEventType() + " to: " + targetUser.getEmail());
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
      System.out.println("[x] Error sending notification to '" + targetUser.getCanonicalName() + "'");
    }
  }
}
