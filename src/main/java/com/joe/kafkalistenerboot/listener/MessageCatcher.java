package com.joe.kafkalistenerboot.listener;

import com.joe.kafkalistenerboot.model.Message;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageCatcher {

    @KafkaListener(topics = "stream-topic", groupId = "sample-group", containerFactory = "messageKafkaListenerFactory")
    public void consume(Message message) {
        System.out.println("Consumed message: " + message);
    }
}
