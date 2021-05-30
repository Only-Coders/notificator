package tech.onlycoders.notificator.service;

import com.google.firebase.database.FirebaseDatabase;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;
import tech.onlycoders.notificator.dto.MessageDTO;

@Service
public class FirebaseService {

  private final FirebaseDatabase database;

  public FirebaseService(FirebaseDatabase database) {
    this.database = database;
  }

  public void storeNotification(MessageDTO message, String collection) throws ExecutionException, InterruptedException {
    var ref = this.database.getReference("notifications/" + collection).push();
    ref.setValueAsync(message).get();
  }
}
