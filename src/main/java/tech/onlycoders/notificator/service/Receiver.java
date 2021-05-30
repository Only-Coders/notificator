package tech.onlycoders.notificator.service;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tech.onlycoders.notificator.repository.UserRepository;
import tech.onlycoders.notificator.dto.MessageDTO;
import tech.onlycoders.notificator.model.User;

import java.util.concurrent.ExecutionException;

@RabbitListener(queues = "onlycoders_notificator")
@Component
public class Receiver {

    private final FirebaseService firebaseService;
    private final UserRepository userRepository;
    private final RMap<String, User> bucket;

    public Receiver(FirebaseService firebaseService, RedissonClient rt, UserRepository userRepository) {
        this.firebaseService = firebaseService;
        this.userRepository = userRepository;
        this.bucket = rt.getMap("EmailUserMap");
    }

    @RabbitHandler
    public void receive(MessageDTO message) throws ExecutionException, InterruptedException {
        System.out.println(" [x] Received '" + message + "'");
        String collection = null;
        if (bucket.containsKey(message.getTo())) {
            collection = bucket.get(message.getTo()).getCanonicalName();
        } else {
            var optionalUser = this.userRepository.findByEmail(message.getTo());
            if (optionalUser.isPresent()) {
                collection = optionalUser.get().getCanonicalName();
                bucket.fastPut(collection, optionalUser.get());
            }
        }

        if (collection != null) {
            this.firebaseService.storeNotification(message, collection);
            System.out.println(" [x] Notification stored '" + message + "'");
        } else {
            System.out.println(" [x] Target user not found " + message.getTo());
        }
    }

}