package tech.onlycoders.notificator.service;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;
import tech.onlycoders.notificator.dto.MessageDTO;
import tech.onlycoders.notificator.model.FCMToken;
import tech.onlycoders.notificator.repository.FCMTokenRepository;

@Service
public class FirebaseService {

  private final FirebaseDatabase database;
  private final FirebaseMessaging messaging;
  private final FCMTokenRepository fcmTokenRepository;

  public FirebaseService(
    FirebaseDatabase database,
    FirebaseMessaging messaging,
    FCMTokenRepository fcmTokenRepository
  ) {
    this.database = database;
    this.messaging = messaging;
    this.fcmTokenRepository = fcmTokenRepository;
  }

  public void storeNotification(MessageDTO message, String collection) throws ExecutionException, InterruptedException {
    var ref = this.database.getReference("notifications/" + collection).push();
    ref.setValueAsync(message).get();
  }

  public void sendPushNotification(MessageDTO messageDto, FCMToken fcmToken) {
    Message message = Message
      .builder()
      .putData("message", messageDto.getMessage())
      .putData("type", messageDto.getEventType().name())
      .putData("from", messageDto.getFrom())
      .setToken(fcmToken.getToken())
      .build();
    try {
      messaging.send(message);
    } catch (FirebaseMessagingException e) {
      this.fcmTokenRepository.deleteById(fcmToken.getId());
    }
  }
}
