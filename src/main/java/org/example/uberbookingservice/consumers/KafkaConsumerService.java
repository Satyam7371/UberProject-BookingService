package org.example.uberbookingservice.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "sample-topic")
    public void listen(String message) {
        System.out.println("Message from topic sample-topic inside booking service: " + message);
    }
}
