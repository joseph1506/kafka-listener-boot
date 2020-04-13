package com.joe.kafkalistenerboot;

import com.joe.kafkalistenerboot.listener.MessageCatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaListenerBootApplication implements CommandLineRunner {

	@Autowired
	MessageCatcher messageCatcher;

	public static void main(String[] args) {
		SpringApplication.run(KafkaListenerBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		messageCatcher.consume();
	}
}
