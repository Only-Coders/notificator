package tech.onlycoders.notificator.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.onlycoders.notificator.repository.UserRepository;

import java.util.Date;

@Component
public class SchedulingService {

  private final UserRepository userRepository;

  public SchedulingService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Scheduled(cron = "59 59 23 * * *")
  public void deleteUsers(){
    System.out.println("[i] --[USER DELETER RUNNING]--");
    var nowMilisec = (new Date()).toInstant().getEpochSecond();
    var userEmails = this.userRepository.getUsersToDelete(nowMilisec);
    for (var email: userEmails) {
      this.userRepository.deleteUser(email);
      System.out.println("[i] User deleted, email '" + email + "'");
    }
    System.out.println("[i] --[USER DELETER FINISHED]--");
  }
}
