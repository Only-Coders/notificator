package tech.onlycoders.notificator.service;

import java.time.Instant;
import java.util.Date;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.onlycoders.notificator.repository.UserRepository;

@Component
public class SchedulingService {

  private final UserRepository userRepository;

  public SchedulingService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Scheduled(cron = "0 */15 * * * *")
  public void deleteUsers() {
    System.out.println("[i] --[USER DELETER RUNNING]--");
    var nowMilliSec = Instant.now().getEpochSecond() * 1000;
    var userEmails = this.userRepository.getUsersToDelete(nowMilliSec);
    for (var email : userEmails) {
      this.userRepository.deleteUser(email);
      System.out.println("[i] User deleted, email '" + email + "'");
    }
    System.out.println("[i] --[USER DELETER FINISHED]--");
  }
}
