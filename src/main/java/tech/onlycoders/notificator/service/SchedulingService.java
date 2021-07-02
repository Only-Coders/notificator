package tech.onlycoders.notificator.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.time.Instant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.onlycoders.notificator.repository.UserRepository;

@Component
public class SchedulingService {

  private final UserRepository userRepository;
  private final FirebaseAuth firebaseAuth;

  public SchedulingService(UserRepository userRepository, FirebaseAuth firebaseAuth) {
    this.userRepository = userRepository;
    this.firebaseAuth = firebaseAuth;
  }

  @Scheduled(cron = "0 */15 * * * *")
  public void deleteUsers() {
    System.out.println("[I] --[USER DELETER RUNNING]--");
    var nowMilliSec = Instant.now().getEpochSecond() * 1000;
    var userEmails = this.userRepository.getUsersToDelete(nowMilliSec);
    for (var email : userEmails) {
      this.userRepository.deleteUser(email);
      try {
        var userRecord = firebaseAuth.getUserByEmail(email);
        firebaseAuth.deleteUser(userRecord.getUid());
      } catch (FirebaseAuthException e) {
        System.out.println("[E] Error removing user " + email + " from firebase auth");
      }
      System.out.println("[I] User deleted, email '" + email + "'");
    }
    System.out.println("[I] --[USER DELETER FINISHED]--");
  }
}
